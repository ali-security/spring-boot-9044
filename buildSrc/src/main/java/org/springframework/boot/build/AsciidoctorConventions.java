/*
 * Copyright 2012-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.build;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.asciidoctor.gradle.jvm.AbstractAsciidoctorTask;
import org.asciidoctor.gradle.jvm.AsciidoctorJExtension;
import org.asciidoctor.gradle.jvm.AsciidoctorJPlugin;
import org.asciidoctor.gradle.jvm.AsciidoctorTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.Sync;

import org.springframework.boot.build.artifacts.ArtifactRelease;
import org.springframework.boot.build.properties.BuildProperties;
import org.springframework.util.StringUtils;

/**
 * Conventions that are applied in the presence of the {@link AsciidoctorJPlugin}. When
 * the plugin is applied:
 *
 * <ul>
 * <li>All warnings are made fatal.
 * <li>The version of AsciidoctorJ is upgraded to 2.4.3.
 * <li>An {@code asciidoctorExtensions} configuration is created.
 * <li>For each {@link AsciidoctorTask} (HTML only):
 * <ul>
 * <li>A task is created to sync the documentation resources to its output directory.
 * <li>{@code doctype} {@link AsciidoctorTask#options(Map) option} is configured.
 * <li>The {@code backend} is configured.
 * </ul>
 * <li>For each {@link AbstractAsciidoctorTask} (HTML and PDF):
 * <ul>
 * <li>{@link AsciidoctorTask#attributes(Map) Attributes} are configured to enable
 * warnings for references to missing attributes, the GitHub tag, the Artifactory repo for
 * the current version, etc.
 * <li>{@link AbstractAsciidoctorTask#baseDirFollowsSourceDir() baseDirFollowsSourceDir()}
 * is enabled.
 * <li>{@code asciidoctorExtensions} is added to the task's configurations.
 * <li>The task is configured to depend on the {@code asciidoctorExtensions} configuraion
 * to ensure that any extensions are built before the task runs. This works around
 * https://github.com/asciidoctor/asciidoctor-gradle-plugin/issues/721.
 * </ul>
 * </ul>
 *
 * @author Andy Wilkinson
 * @author Scott Frederick
 */
class AsciidoctorConventions {

	private static final String ASCIIDOCTORJ_VERSION = "2.4.3";

	void apply(Project project) {
		project.getPlugins().withType(AsciidoctorJPlugin.class, (asciidoctorPlugin) -> {
			makeAllWarningsFatal(project);
			upgradeAsciidoctorJVersion(project);
			Configuration asciidoctorExtensions = createAsciidoctorExtensionsConfiguration(project);
			project.getTasks()
				.withType(AbstractAsciidoctorTask.class,
						(asciidoctorTask) -> configureAsciidoctorTask(project, asciidoctorTask, asciidoctorExtensions));
		});
	}

	private void makeAllWarningsFatal(Project project) {
		project.getExtensions().getByType(AsciidoctorJExtension.class).fatalWarnings(".*");
	}

	private void upgradeAsciidoctorJVersion(Project project) {
		project.getExtensions().getByType(AsciidoctorJExtension.class).setVersion(ASCIIDOCTORJ_VERSION);
	}

	private Configuration createAsciidoctorExtensionsConfiguration(Project project) {
		return project.getConfigurations().create("asciidoctorExtensions", (configuration) -> {
			project.getConfigurations()
				.matching((candidate) -> "dependencyManagement".equals(candidate.getName()))
				.all(configuration::extendsFrom);
			configuration.getDependencies()
				.add(project.getDependencies()
					.create("io.spring.asciidoctor.backends:spring-asciidoctor-backends:0.0.5"));
			configuration.getDependencies()
				.add(project.getDependencies().create("org.asciidoctor:asciidoctorj-pdf:1.5.3"));
		});
	}

	private void configureAsciidoctorTask(Project project, AbstractAsciidoctorTask asciidoctorTask,
			Configuration asciidoctorExtensions) {
		asciidoctorTask.configurations(asciidoctorExtensions);
		asciidoctorTask.dependsOn(asciidoctorExtensions);
		configureCommonAttributes(project, asciidoctorTask);
		configureOptions(asciidoctorTask);
		configureForkOptions(asciidoctorTask);
		asciidoctorTask.baseDirFollowsSourceDir();
		createSyncDocumentationSourceTask(project, asciidoctorTask);
		if (asciidoctorTask instanceof AsciidoctorTask task) {
			boolean pdf = task.getName().toLowerCase(Locale.ROOT).contains("pdf");
			String backend = (!pdf) ? "spring-html" : "spring-pdf";
			task.outputOptions((outputOptions) -> outputOptions.backends(backend));
		}
	}

	private void configureCommonAttributes(Project project, AbstractAsciidoctorTask asciidoctorTask) {
		String buildType = BuildProperties.get(project).buildType().toIdentifier();
		ArtifactRelease artifacts = ArtifactRelease.forProject(project);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("attribute-missing", "warn");
		attributes.put("github-tag", determineGitHubTag(project));
		attributes.put("build-type", buildType);
		attributes.put("artifact-release-type", artifacts.getType());
		attributes.put("build-and-artifact-release-type", buildType + "-" + artifacts.getType());
		attributes.put("artifact-download-repo", artifacts.getDownloadRepo());
		attributes.put("revnumber", project.getVersion());
		asciidoctorTask.attributes(attributes);
	}

	// See https://github.com/asciidoctor/asciidoctor-gradle-plugin/issues/597
	private void configureForkOptions(AbstractAsciidoctorTask asciidoctorTask) {
		asciidoctorTask.jvm((options) -> options.jvmArgs("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
				"--add-opens", "java.base/java.io=ALL-UNNAMED"));
	}

	private String determineGitHubTag(Project project) {
		String version = "v" + project.getVersion();
		return (version.endsWith("-SNAPSHOT")) ? "3.2.x" : version;
	}

	private void configureOptions(AbstractAsciidoctorTask asciidoctorTask) {
		asciidoctorTask.options(Collections.singletonMap("doctype", "book"));
	}

	private void createSyncDocumentationSourceTask(Project project, AbstractAsciidoctorTask asciidoctorTask) {
		Sync syncDocumentationSource = project.getTasks()
			.create("syncDocumentationSourceFor" + StringUtils.capitalize(asciidoctorTask.getName()), Sync.class);
		File syncedSource = project.getLayout()
			.getBuildDirectory()
			.dir("docs/src/" + asciidoctorTask.getName())
			.get()
			.getAsFile();
		syncDocumentationSource.setDestinationDir(syncedSource);
		syncDocumentationSource.from("src/docs/");
		asciidoctorTask.dependsOn(syncDocumentationSource);
		asciidoctorTask.getInputs()
			.dir(syncedSource)
			.withPathSensitivity(PathSensitivity.RELATIVE)
			.withPropertyName("synced source");
		asciidoctorTask.setSourceDir(project.relativePath(new File(syncedSource, "asciidoc/")));
	}

}
