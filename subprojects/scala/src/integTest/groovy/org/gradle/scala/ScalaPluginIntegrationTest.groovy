/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.scala

import groovy.transform.NotYetImplemented
import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import spock.lang.Issue

class ScalaPluginIntegrationTest extends AbstractIntegrationSpec {
    @Issue("https://issues.gradle.org/browse/GRADLE-3094")
    def "can apply scala plugin"() {
        file("build.gradle") << """
apply plugin: "scala"

task someTask
"""

        expect:
        succeeds("someTask")
    }

    @Issue("https://github.com/gradle/gradle/issues/6558")
    def "can build in parallel with lazy tasks"() {
        settingsFile << """
            include 'a', 'b', 'c', 'd'
        """
        buildFile << """
            allprojects {
                repositories {
                    ${jcenterRepository()}
                }
                plugins.withId("scala") {
                    dependencies {
                        compile("org.scala-lang:scala-library:2.12.6")
                    }
                }
            }
        """
        ['a', 'b', 'c', 'd'].each { project ->
            file("${project}/build.gradle") << """
                plugins {
                    id 'scala'
                }
            """
            file("${project}/src/main/scala/${project}/${project.toUpperCase()}.scala") << """
                package ${project}
                trait ${project.toUpperCase()}
            """
        }
        file("a/build.gradle") << """
            dependencies {
              compile(project(":b"))
              compile(project(":c"))
              compile(project(":d"))
            }
        """

        expect:
        succeeds(":a:classes", "--parallel")
    }

    @Issue("https://github.com/gradle/gradle/issues/6735")
    def "can depend on the source set of another Java project"() {
        settingsFile << """
            include 'java', 'scala'
        """
        buildFile << """
            allprojects {
                repositories {
                    ${jcenterRepository()}
                }
            }
            project(":java") {
                apply plugin: 'java'
            }
            project(":scala") {
                apply plugin: 'scala'
                dependencies {
                    compile("org.scala-lang:scala-library:2.12.6")
                    compile(project(":java").sourceSets.main.output)
                }
            }
        """
        file("java/src/main/java/Bar.java") << """
            public class Bar {}
        """
        file("scala/src/test/scala/Foo.scala") << """
            trait Foo {
                val bar: Bar
            }
        """
        expect:
        succeeds(":scala:testClasses")
    }

    @Issue("https://github.com/gradle/gradle/issues/6750")
    def "can depend on Scala project from other project"() {
        settingsFile << """
            include 'other', 'scala'
        """
        buildFile << """
            allprojects {
                repositories {
                    ${jcenterRepository()}
                }
            }
            project(":other") {
                apply plugin: 'base'
                configurations {
                    conf
                }
                dependencies {
                    conf(project(":scala"))
                }
                task resolve {
                    dependsOn configurations.conf
                    doLast {
                        println configurations.conf.files
                    }
                }
            }
            project(":scala") {
                apply plugin: 'scala'

                dependencies {
                    compile("org.scala-lang:scala-library:2.12.6")
                }
            }
        """
        file("scala/src/main/scala/Bar.scala") << """
            class Bar {
            }
        """
        expect:
        succeeds(":other:resolve")
    }

    @NotYetImplemented
    @Issue("https://github.com/gradle/gradle/issues/7014")
    def "can use Scala with war and ear plugins"() {
        settingsFile << """
            include 'war', 'ear'
        """
        buildFile << """
            allprojects {
                repositories {
                    ${jcenterRepository()}
                }
                apply plugin: 'scala'

                dependencies {
                    compile("org.scala-lang:scala-library:2.12.6")
                }
            }
            project(":war") {
                apply plugin: 'war'
            }
            project(":ear") {
                apply plugin: 'ear'
                dependencies {
                    deploy project(path: ':war', configuration: 'archives')
                }
            }
        """
        file("war/src/main/scala/Bar.scala") << """
            class Bar {
            }
        """
        expect:
        succeeds(":ear:assemble")
        file("ear/build/tmp/ear/application.xml").text == """<?xml version="1.0"?>
<application xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/application_6.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="6">
  <display-name>ear</display-name>
  <module>
    <web>
      <web-uri>war.war</web-uri>
      <context-root>war</context-root>
    </web>
  </module>
  <library-directory>lib</library-directory>
</application>
"""
    }

    @NotYetImplemented
    @Issue("https://github.com/gradle/gradle/issues/6849")
    def "can publish test-only projects"() {
        using m2
        settingsFile << """
            rootProject.name = "scala"
        """
        buildFile << """
            apply plugin: 'scala'
            apply plugin: 'maven'

            repositories {
                ${jcenterRepository()}
            }
            dependencies {
                compile("org.scala-lang:scala-library:2.12.6")
            }
        """
        file("src/test/scala/Foo.scala") << """
            class Foo
        """
        expect:
        succeeds("install")
    }
}
