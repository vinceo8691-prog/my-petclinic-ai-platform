# 1. Use a monorepo for the PetClinic AI platform

Date: 2026-09-30

## Status

Accepted

## Context

The PetClinic AI platform is made up of three applications that change together far
more often than they change independently:

- `spring-petclinic-rest` — the Spring Boot REST backend
  that owns all business data
- `petclinic-ui` — the React/TypeScript frontend, which calls both PetClinic and the
  AI Agent
- `petclinic-ai-agent` — the Spring Boot AI agent, which never accesses the PetClinic
  database directly and instead reaches it through PetClinic's REST API

Because the AI Agent and the UI are both consumers of contracts defined in
`spring-petclinic-rest` (REST endpoints, DTOs, OpenAPI specs), a single feature — for example,
adding a new AI-initiated action that requires human confirmation — routinely touches
all three applications in one coherent change: a new endpoint in the API, a new tool
definition in the AI Agent, and a new confirmation UI in the frontend.

With separate repositories, this kind of change requires coordinating versions across
repos, bumping a shared contract/client dependency, and sequencing merges and
deployments by hand. For a small team (today, effectively a single developer) building
and iterating on all three applications simultaneously, that coordination overhead was
judged to outweigh the benefits of separate repos (independent access control,
independent release cadence, smaller per-repo CI).

## Decision

We will keep `spring-petclinic-rest`, `petclinic-ui`, and `petclinic-ai-agent` in a single
monorepo, with each application living in its own top-level directory and architecture
rules (ownership of data, allowed call directions) documented centrally in
`CLAUDE.md`.

Cross-cutting documentation — ADRs and other architecture docs — lives in `docs/` at
the repo root, rather than being duplicated or split across per-app repos.

## Consequences

**Positive:**

- A single commit (and a single PR) can express a cross-app change — e.g. a new API
  endpoint plus the AI Agent tool and UI component that use it — instead of being
  split across repos and versioned dependencies.
- Architecture rules and shared context (`CLAUDE.md`, ADRs) apply to the whole
  platform by default, with no risk of the three apps drifting onto inconsistent
  conventions.
- Refactors that touch a contract (e.g. renaming a DTO field) can be done atomically
  and verified against all consumers in the same change, instead of landing a breaking
  change in one repo and hoping downstream repos catch up.
- Local development and code search span the whole platform; there's one clone, one
  `CLAUDE.md`, one place to look for how the pieces fit together.

**Negative / trade-offs accepted:**

- CI will need to scope itself (e.g. path filters) so that a change to
  `petclinic-ui` doesn't trigger a full rebuild/redeploy of `spring-petclinic-rest` and
  `petclinic-ai-agent`, or build times will grow unnecessarily as the platform grows.
- All three applications currently share one set of repo-level permissions; if
  access needs to be restricted per-application in the future (e.g. a vendor team
  that should only see `petclinic-ui`), that will require either a migration out of
  the monorepo or finer-grained access controls layered on top of it.
- Independent release cadences are harder to express — tagging/releasing one
  application without implying anything about the others needs explicit convention
  (e.g. per-app tags) rather than being the default.
- As the platform grows, build/test tooling may need investment (e.g. a task runner
  or monorepo build tool) to avoid every change paying the cost of building all three
  applications.

## Revisit if

- The applications stop changing together (e.g. `petclinic-ai-agent` is spun out to
  a separate team with its own release cycle and access requirements).
- CI build times become a bottleneck that path-scoped pipelines can't solve.
