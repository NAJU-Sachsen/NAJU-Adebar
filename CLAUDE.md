# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Adebar is a management tool for NAJU Sachsen. It tracks volunteers (persons with participant, activist, referent and parent profiles), events, local groups, projects and newsletters. It is a server-rendered web app built with Spring Boot and Thymeleaf, with Bootstrap 3 and jQuery served via WebJars. The active line of development is the `develop` branch; `main`/`master` are years behind it.

Stack: Java 25, Spring Boot 4.1 (Spring Framework 7, Spring Security 7, Hibernate 7, Jakarta EE 11), Thymeleaf 3.1, QueryDSL (OpenFeign fork, `com.querydsl` packages), JavaMoney, Guava, JUnit Jupiter and AssertJ.

## Commands

The toolchain is pinned in `.sdkmanrc`. Run `sdk env` in every new shell, or `sdk env install` once to install the toolchain.

```shell
mvn clean package                         # build + run all tests (H2, no DB needed)
mvn spring-boot:run                       # run at http://localhost:8080 (needs MySQL, see below)
mvn test -Dtest=PersonUnitTest            # single test class
mvn test -Dtest=PersonUnitTest#methodName # single test method
```

QueryDSL `Q*` classes are generated into `target/generated-sources/annotations` by the annotation processor that is configured on the `maven-compiler-plugin`.

## Database

- Runtime: MySQL (8.4 LTS or 9.7 LTS) at `localhost:3306/adebar`, user/password `adebar`, with `ddl-auto=update`. The schema has no migration tool; Hibernate's schema update is the only schema management.
- Tests: H2 in-memory (`src/test/resources/application.properties`). Most tests are `@SpringBootTest` with `@Transactional`.
- `startup.properties` (`adebar.admin.*`): `DatabaseInitializer` creates the admin person and account from these values if no admin account exists.
- ID generation must stay compatible with the existing production data:
  - Entities with numeric `@GeneratedValue` IDs (`LocalGroup`, `Project`, `Board`, `Newsletter`, `RoomSpecification`) explicitly use the shared legacy `hibernate_sequence` table with `allocationSize = 1`. Don't switch them to Hibernate's default per-entity `*_seq` tables, which would hand out IDs that already exist.
  - `Reservation` IDs (`NumericEntityId`) come from the `AUTO_INCREMENT` column of `event_reservations`.
- JPQL is type-checked strictly: compare booleans with `true`/`false`, not `0`/`1`. Native queries only accept basic parameter values (pass `PersonId.toString()`, not a `Person` or `PersonId`).

## Architecture

Root package is `de.naju.adebar`. The code loosely follows DDD; the annotations in `documentation` (`@ValueObject`, `@BusinessRule`, `@JpaOnly`, ...) are markers only.

- `model/`: domain entities, value objects, factories, repositories and domain exceptions per subsystem (`persons`, `events`, `chapter`, `newsletter`, `core`).
  - `Person` (ID `PersonId`, UUID string) has optional profiles sharing its primary key. Create persons through `PersonFactory.buildNew(...)...create()`.
  - Repositories come in pairs. `ReadOnly*Repository` is a read-only view built on `infrastructure/ReadOnlyRepository`; it filters out archived persons via `@Query`. The writable `*Repository` extends both `CrudRepository` and the read-only view, so overrides must be declared with return types compatible with `CrudRepository` (e.g. `List`).
- `app/`: application services: managers (`Persistent*Manager`), search, newsletter, release notes (`news`), security (`UserAccount`, roles, `UserAccountService`) and the filter framework:
  - `app/filter` models filter criteria as field/filter objects. `querydsl/QueryPredicateGeneratingVisitor` turns them into QueryDSL predicates; `app/persons/filter` maps the fields to `QPerson` paths.
  - `app/filter/legacy` and `app/persons/legacy` hold the older stream-based filters, which are still in use.
- `web/`: MVC controllers (`web/controller`, JSON endpoints in `web/controller/api`), form objects and form↔entity converters (`web/validation`, `ValidatingEntityFormConverter` = converter plus Spring `Validator`), and view-model/rendering helpers (`web/model`, `web/services`, e.g. participants table, CSV export). `services/conversion` holds Spring `Converter`s for path variables and request parameters, registered in `WebConfiguration`.
- `infrastructure/`: JPA `AttributeConverter`s, Spring config, the custom Thymeleaf dialect (`infrastructure/thymeleaf/ExtendedDialect`) and a raw SQL executor for the admin SQL console.

Security (`infrastructure/config/security`): a `SecurityFilterChain` with form login. The `RoleHierarchy` bean (`ADMIN > JUBIREF > EMPLOYEE > FOEJ > CHAIRMAN > TREASURER > BOARD_MEMBER > USER`) is applied to URL rules, `@PreAuthorize` (via `@EnableMethodSecurity`) and `sec:authorize` in templates.

## Templates

- Templates live in `src/main/resources/templates` and are German-language. Use full fragment expressions: `th:replace="~{navigation :: topNav(page='events')}"`.
- Thymeleaf 3.1 restrictions apply:
  - Instead of `#request` or `#httpServletRequest`, read request parameters via `${param['name']}` (a `String[]`).
  - `T(java.*)` static access is not allowed.
- Error pages (`templates/error/4xx.html`, `5xx.html`) use Spring Boot's error attributes (`status`, `path`, `exception`, `message`, `trace`), enabled through the `spring.web.error.*` properties.
- WebJar URLs are versionless (`/webjars/jquery/jquery.min.js`) and resolved by `webjars-locator-lite`. It only supports `org.webjars` and `org.webjars.npm` artifacts.
