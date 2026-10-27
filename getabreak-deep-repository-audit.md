# getabreak — Deep Repository Audit Findings

**Repository:** https://github.com/mizan989/getabreak-focus_app  
**Audit date:** 27 October 2026  
**Latest commit reviewed:** [`cf260bd`](https://github.com/mizan989/getabreak-focus_app/commit/cf260bd2874717d1959afc3cda647d0eea7415da)  
**Previous commit:** [`97a9bfb`](https://github.com/mizan989/getabreak-focus_app/commit/97a9bfbfbc7acdf92a580f3d391c8403b39b74e0)  

## 1. Executive summary

The latest commit makes meaningful progress on the `getabreak` rebrand. The app display name, English and German store titles, repository links, Gradle root project name, installer repository URLs, funding configuration, and pull-request branch filter have been updated.

Remaining items:

1. **Premium/support wording** may imply donations or paid extras even though the FOSS support provider says features are free and support links are removed.
2. **Installer scripts depend on a GitHub Release APK**, but the releases endpoint returned an empty list during this audit. Fresh installation through those scripts may fail until a compatible release asset is published.
3. **“Break Doom Scrolling”** remains in a resource string and may be an old product-name remnant. Confirm whether it is an intentional feature name before editing.
4. **CI/build status is unverified.** The GitHub API reported zero workflow runs at audit time. No local build or test was run as part of this review.

Do not rename internal Android package names or technical classes merely to remove every occurrence of `DetoxDroid`; those identifiers can have compatibility and reference implications.

## 2. Improvements in the latest commit

Commit [`cf260bd`](https://github.com/mizan989/getabreak-focus_app/commit/cf260bd2874717d1959afc3cda647d0eea7415da) is titled “feat: finalize getabreak branding, store metadata, and remove legacy funding.”

Compared with the preceding commit, the public diff shows these changes:

- Removed `.github/FUNDING.yml`.
- Updated `.github/workflows/pr.yaml` to include `main` alongside `master` and `develop`.
- Updated README branding and repository links.
- Updated `app/src/main/res/values/strings.xml` and `app/src/main/res/values-de/strings.xml`.
- Updated English and German Fastlane store descriptions and titles.
- Updated repository links in the permission screen.
- Updated installer scripts to query releases from `mizan989/getabreak-focus_app`.
- Changed `settings.gradle.kts` root project name to `getabreak`.

Current English and German store titles both read `getabreak`. Targeted checks of the inspected store descriptions no longer found the old upstream name or donation-platform references.

## 3. Remaining findings

### Finding 1 — Premium/support wording needs review

**Priority:** Medium  
**File:** [`app/src/main/res/values/strings.xml`](https://github.com/mizan989/getabreak-focus_app/blob/main/app/src/main/res/values/strings.xml)

Relevant resource keys:

- `premium_tile_subtitle` — “Support the project & unlock power-user extras”
- `premium_sheet_title_generic` — “Support getabreak”
- `premium_sheet_subtitle` — “Free, open source, no ads. Maintained by one person.”

The FOSS support implementation at `app/src/foss/java/com/flx_apps/digitaldetox/premium/PremiumSupport.kt` says all digital wellbeing features are free and unlocked, with empty support links and no Bitcoin address. The FOSS premium-sheet action presents an “Unlock all features” action and says there are no subscriptions or paywalls.

**Why it matters:** The remaining tile/sheet copy can give users the impression that they should donate or pay to unlock extras, which conflicts with the FOSS experience. “Maintained by one person” is not inherently incorrect, but should be retained only if accurate and desired.

**Recommended action:**
- Inspect how the premium tile and sheet are shown in the FOSS flavor.
- Rewrite visible FOSS copy to clearly state that features are free and available without payment.
- Do not remove or rewrite Google Play billing copy without checking the `googlePlay` flavor; it may intentionally have a different purchase model.
- Check corresponding German translations in `app/src/main/res/values-de/strings.xml`.

### Finding 2 — Installer scripts depend on a release APK, but no release was found

**Priority:** High for users of the installer scripts; otherwise release-readiness issue  
**Files:** `install/install_windows.bat`, `install/install_linux.sh`, `install/install_mac.sh`

All three scripts query the GitHub Releases API for the latest release and look for a versioned APK asset similar to `app-3.0.1.apk`.

**Evidence:** The releases endpoint returned an empty list during this audit:

https://api.github.com/repos/mizan989/getabreak-focus_app/releases

Releases page: https://github.com/mizan989/getabreak-focus_app/releases

**Impact:** If the scripts need to download an APK and no matching release/asset exists, a fresh install can fail. The exact user-visible failure depends on each script's fallback and error handling. This report did not execute the scripts, so exact failure messages are not verified.

**Recommended action:**
1. Decide whether release-based installation is the intended distribution path.
2. If yes, publish a release with the exact APK asset naming convention expected by all three scripts.
3. If not, update all three scripts consistently to use a verified alternative.
4. Check no-release, no-matching-asset, network-error, and download-failure cases.
5. Keep Windows, Linux, and macOS behavior aligned.

Do not assume that creating a release fixes the issue: verify the asset name and API response against the actual matching logic in every script.

### Finding 3 — “Break Doom Scrolling” remains in a user-facing resource

**Priority:** Low / needs confirmation  
**File:** [`app/src/main/res/values/strings.xml`](https://github.com/mizan989/getabreak-focus_app/blob/main/app/src/main/res/values/strings.xml)

Relevant lines in the inspected version:
- Line 591 references `Break "Doom Scrolling"`.
- Line 601 defines `feature_doomScrolling` as `Break "Doom Scrolling"`.

**Why it matters:** Since the product is now named `getabreak`, the phrase could be an old product name rather than a deliberate feature label.

**Recommended action:**
- Trace where `feature_doomScrolling` is displayed and compare it with the German translation.
- If it is an inherited feature name, rename only the user-facing text to a clear description such as “Doom scrolling interruption,” if that accurately describes the behavior.
- Keep ordinary English uses of “break” (such as a pause or break screen) unchanged.
- Do not perform a global find-and-replace on `Break`.

### Finding 4 — Build and test status is unverified

**Priority:** High before release  
**Workflow:** `.github/workflows/pr.yaml`

The workflow now includes `main` in its pull-request branch filter, fixing the previously identified branch mismatch. However, the repository's GitHub Actions runs endpoint reported **zero workflow runs** at audit time.

The workflow contains these checks:

```bash
./gradlew ktlintCheck || true
./gradlew testFossDebugUnitTest
./gradlew assembleFossDebug
```

**Important distinction:** `ktlintCheck || true` allows the workflow to continue even if lint fails. This means lint is advisory rather than a required passing check.

**Recommended action:**
1. Run unit tests and the FOSS debug build locally or through a real CI run.
2. Review complete logs, not just the workflow's existence.
3. Decide whether lint failures should block the build. If lint is intended to be mandatory, remove `|| true` only after confirming and fixing existing lint failures.
4. Confirm the workflow is triggered by a real pull request targeting `main`.
5. Do not report tests as passing unless a run actually completes successfully.

## 4. Branding references that should not be blindly renamed

The current manifest still contains technical identifiers such as:

- `.DetoxDroidApplication`
- `@style/Theme.DetoxDroid`
- `.system_integration.DetoxDroidAccessibilityService`
- `DetoxDroidDeviceAdminReceiver`
- `com.flx_apps.digitaldetox`

These are package/class/style identifiers, not necessarily visible product branding. Renaming them can require coordinated code, manifest, device-owner command, migration, and compatibility changes.

The manifest comment referring to uninstalling “DetoxDroid” is stale documentation and can be corrected independently, but do not rename implementation identifiers as part of a cosmetic branding pass without a separate, tested migration plan.

Ordinary feature language such as “take a break,” “break screen,” or “break up doomscrolling” is not automatically a brand reference.

## 5. Suggested remediation order

1. **Release readiness:** Confirm whether the installers should download from GitHub Releases. Publish the expected APK asset or implement and test a verified fallback.
2. **FOSS copy:** Remove donation/paid-extra implications from FOSS-visible copy and align English/German strings.
3. **Feature label:** Verify and resolve the “Break Doom Scrolling” wording without global replacement.
4. **CI evidence:** Trigger a workflow and confirm unit tests/build actually complete.
5. **Final targeted scan:** Search user-visible strings, Fastlane metadata, README, installer output, manifest labels, and funding files for stale branding. Classify each match as user-facing, ordinary feature wording, or internal technical identifier.
6. **Review the diff:** Use `git diff --check` and inspect `git diff` before committing. Avoid unrelated changes.

## 6. Verification status and limitations

| Check | Status during this audit |
|---|---|
| Latest commit and parent diff inspected | Verified through GitHub API |
| App display name and store titles | Verified as `getabreak` |
| Funding file removed | Verified in commit diff |
| PR workflow includes `main` | Verified in current workflow file |
| Root project name | Verified as `getabreak` |
| Store description stale donation/upstream wording | Targeted checks no longer found those references in inspected description lines |
| Installer release availability | No releases found at audit time |
| Installer scripts executed on Windows/macOS/Linux | Not tested |
| Unit tests executed | Not tested |
| FOSS debug APK build | Not tested |
| Runtime behavior on a physical Android device | Not tested |
| Every repository file exhaustively searched | No; this was a targeted deep review, not a complete local checkout/static-analysis run |

## 7. Useful links

- Repository: https://github.com/mizan989/getabreak-focus_app
- Latest commit: https://github.com/mizan989/getabreak-focus_app/commit/cf260bd2874717d1959afc3cda647d0eea7415da
- Compare changes: https://github.com/mizan989/getabreak-focus_app/compare/97a9bfbfbc7acdf92a580f3d391c8403b39b74e0...cf260bd2874717d1959afc3cda647d0eea7415da
- Releases: https://github.com/mizan989/getabreak-focus_app/releases
- Main strings: https://github.com/mizan989/getabreak-focus_app/blob/main/app/src/main/res/values/strings.xml
- German strings: https://github.com/mizan989/getabreak-focus_app/blob/main/app/src/main/res/values-de/strings.xml
- Pull-request workflow: https://github.com/mizan989/getabreak-focus_app/blob/main/.github/workflows/pr.yaml

---

**Final assessment:** The latest commit resolves most previously reported rebranding and repository housekeeping issues. Prioritize the installer/release dependency and FOSS support wording, then obtain real build/test evidence before treating the cleanup as release-ready.
