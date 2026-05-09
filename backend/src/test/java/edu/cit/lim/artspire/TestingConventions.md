# ArtSpire Backend Testing Conventions

## Naming

Use `methodOrEndpoint_whenCondition_expectedResult`.

Examples:

- `register_whenEmailIsNew_savesEncodedUserAndReturnsSuccessResponse`
- `login_whenPasswordDoesNotMatch_throwsUnauthorized`
- `findByEmail_whenUserDoesNotExist_returnsEmptyOptional`

## Test Types

- Unit tests: no Spring context, use Mockito for dependencies.
- MockMvc controller tests: use `@WebMvcTest`, mock the service/facade layer, assert HTTP status and JSON.
- Integration tests: use `@SpringBootTest`, `@AutoConfigureMockMvc`, and the `test` profile.
- Repository tests: use `@DataJpaTest` and H2.

## Test Data

Use `TestData` for common valid objects and constants. Keep per-test edge-case values inline so the scenario is obvious.

## Future Artwork CRUD Tests

When artwork classes exist, add tests matching these scenarios:

- `createArtwork_whenRequestIsValid_returnsCreatedArtwork`
- `createArtwork_whenTitleIsBlank_returnsValidationError`
- `getArtwork_whenArtworkExists_returnsArtwork`
- `getArtwork_whenArtworkIsMissing_returnsNotFound`
- `updateArtwork_whenOwnerMatches_updatesArtwork`
- `deleteArtwork_whenArtworkExists_removesArtwork`
- `uploadArtworkImage_whenFileIsEmpty_returnsBadRequest`

Recommended packages:

- `src/test/java/edu/cit/lim/artspire/artwork/ArtworkServiceTest.java`
- `src/test/java/edu/cit/lim/artspire/artwork/ArtworkControllerTest.java`
- `src/test/java/edu/cit/lim/artspire/artwork/ArtworkIntegrationTest.java`
- `src/test/java/edu/cit/lim/artspire/artwork/ArtworkRepositoryTest.java`

## Future Admin/JWT Tests

When admin and JWT security classes exist, add tests for:

- Missing token returns `401 Unauthorized`.
- Invalid token returns `401 Unauthorized`.
- Valid user token is forbidden from admin endpoints with `403 Forbidden`.
- Valid admin token can access admin endpoints.
- Disabled or deleted users cannot authenticate.
- Token subject maps to the expected application user.
