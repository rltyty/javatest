# javatest
A Neovim-powered Java playground.

[jtkit](https://github.com/rltyty/jtkit), which facilitates the creation of
test cases and the provision of test data, has been spun off into a standalone
project.

## Requirement

Add [jtkit](https://github.com/rltyty/jtkit) as a dependency to `pom.xml`

```xml
    <dependency>
      <groupId>io.github.rltyty</groupId>
      <artifactId>jtkit</artifactId>
      <version>0.7.0</version>
      <scope>test</scope>
    </dependency>
```

### [Neovim Configuration]

[nvim.lua](./nvim.lua) provides Neovim key mappings and project-specific
template configuration to streamline Maven test development.

- **Maven test shortcuts:** Run tests on the current class or specified
  methods, profile test execution time, enable verbose logging, and perform
  common Maven operations.
- **Test template generation:** Configure project-specific code templates with
  automatic substitution of metadata, including author, creation date, package
  name, and class name.

The template configuration assumes a Maven-style Java project structure and
uses `src/templates` as the template directory.
