# Review: 7-setup-rest-surface

Out of scope: REST exception handling, auth, tests.

## Things to be aware of

- [ ] **Migration history was rewritten.** `V001` was edited and `V002` deleted, but both are already on `main`, so existing local DBs fail Flyway validation (wipe the compose volume). From now on add new migrations instead of editing old ones.
- [ ] **The create path breaks a nullness contract.** `TaskRestMapper.toDTO(CreateTaskRequest)` builds a `TaskDTO` with `key == null`, but `TaskDTO.key` is declared `@NonNull`. Make it `@Nullable`, or pass a dedicated create command into `create`.
- [ ] **`TaskFilterRequest` isn't validated.** `@Valid` has no effect because there are no constraints, and a `Range` with `min > max` quietly returns an empty result. Also confirm that nested binding of the generic `Range<Instant>` from query params (`createdAt.min=…`) works.

## Minor / style

- [ ] Simplify the `Location` header with `ServletUriComponentsBuilder.fromCurrentRequest().path("/{key}").buildAndExpand(task.key())` instead of `MvcUriComponentsBuilder`, which proxies the controller and relies on the overloaded `get`.
- [ ] Consider `ETag` / `If-Match` instead of a `version` query param on PUT/DELETE (412 Precondition Failed).
- [ ] Formatting: a blank line added to `TaskDTO`, a double blank line at the end of `Probe`.
