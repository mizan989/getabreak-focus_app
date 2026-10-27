# getabreak — Final Branding and Repository Cleanup

## Purpose

Perform a **strictly scoped final cleanup** of the existing Android project:

- Repository: `https://github.com/mizan989/getabreak-focus_app`
- New product name: **getabreak** (all lowercase)
- Previous product name: `Break`
- Original upstream project: DetoxDroid

The goal is to make the product name **getabreak** consistent throughout the app and its project-owned public-facing materials, and fix the specific leftover branding/funding issues listed below.

This is a targeted cleanup, **not a redesign, refactor, feature rewrite, or modernization task**.

## Non-negotiable instructions

1. **Do not hallucinate.** Do not claim a file, string, screen, workflow, link, or feature exists unless you verify it in the checked-out repository.
2. **Inspect before editing.** Read relevant files in the current working tree. Search for actual occurrences of `Break`, `break`, `DetoxDroid`, `Detox Droid`, `flxapps`, old repository URLs, donation wording, and old funding URLs before deciding what to change.
3. **Do not make unnecessary changes elsewhere.** Modify only files directly necessary for the name change and the concrete issues documented here. No unrelated formatting changes, refactors, dependency upgrades, architecture changes, feature changes, or visual redesigns.
4. **Do not blindly replace every occurrence of `break`.** It is also an ordinary English word and may appear in class names, feature names, identifiers, resources, descriptions, URLs, or functional concepts. Review each occurrence in context. Rename user-facing product branding where appropriate; preserve functional identifiers unless renaming is demonstrably necessary and safe.
5. **Preserve app behavior.** Do not remove, disable, or change existing wellbeing features, app rules, accessibility behavior, usage tracking, permission flows, notifications, widgets, storage, or other functionality as part of this cleanup.
6. **Preserve upstream attribution and GPL-3.0 compliance.** Do not delete the `LICENSE`, required copyright notices, or required source attribution. Keep the README's upstream acknowledgement accurate. Do not imply getabreak created the original DetoxDroid code.
7. **Keep the current design.** Retain the existing monochrome visual direction, approved logo/assets, restrained subtle-glass treatment, typography, spacing, layout, and interactions. Do not invent a new logo or add visual elements.
8. **No speculative cleanup.** If a proposed change cannot be justified by a verified occurrence or requirement below, leave it untouched and report it rather than guessing.
9. **Do not claim tests passed unless you actually run them and see a successful result.**
10. **Review the final diff.** Before finishing, inspect `git diff` and confirm every change is in scope.

## Required work

### 1. Standardize the product name to `getabreak`

Use **`getabreak`** as the exact product brand, all lowercase, wherever the product's name is displayed or declared in project-owned branding.

Audit at minimum:

- App display name and launcher label
- App title/top bar and About screen
- Splash screen text or branding, if present
- Notification titles, descriptions, and ongoing-service text
- Quick Settings tile labels and system-visible labels
- Device-admin labels and user-visible system explanations
- Accessibility-service labels/descriptions
- Widget names, widget configuration titles, and widget labels
- Settings/preferences and premium/support sheet copy
- In-app links and project/repository references
- README, screenshots/metadata references, and store listing text
- Gradle root project name
- App icon and image assets that visibly contain the old name
- English and other maintained translations that contain user-facing product branding

Do not assume every item exists. Verify each one and record which files were actually changed.

#### Naming rules

- Product name: `getabreak`.
- Do not use `GetABreak`, `Get a Break`, `Break`, or `DetoxDroid` as the displayed product name in getabreak-owned UI or marketing copy.
- Do not change technical class/package/namespace identifiers such as `DetoxDroidApplication`, `DetoxDroidState`, or `digitaldetox` merely to make the text search clean. Renaming code symbols and namespaces can cause broad, unnecessary changes and is outside this task unless a verified user-facing defect cannot be fixed another way.
- Preserve terms where `break` describes an actual feature or ordinary concept, such as an emergency brake, pause/break action, or doomscrolling break. Do not distort feature descriptions to force the word out.
- Update user-facing upstream links to the getabreak repository where appropriate, but retain upstream attribution in documentation and legal notices.
- Do not invent a new domain, website, email, social profile, package name, application ID, or release destination.

### 2. Fix verified old repository links

Where the UI provides a link to the project's own repository or issue tracker, use the verified repository:

- Repository: `https://github.com/mizan989/getabreak-focus_app`
- Issues: `https://github.com/mizan989/getabreak-focus_app/issues/new`

Update only links that are meant to lead users to this fork's repository or issue tracker. Do not replace links to upstream source, libraries, dependencies, licences, or external services indiscriminately.

### 3. Remove outdated donation/funding presentation from the FOSS experience

The intended FOSS experience is free and has no donation/tip-jar prompts.

Inspect and fix these verified issues:

- `.github/FUNDING.yml`: remove old developer funding entries that advertise `flxapps`/the upstream developer, or remove the file if it contains no legitimate getabreak funding configuration and is solely the inherited upstream funding setup.
- About screen copy: remove obsolete “tip jar” wording if it is still displayed.
- Premium/support sheet: ensure it does not ask users to support/donate to the upstream developer or imply paid features.
- Ensure the FOSS implementation continues to make all wellbeing features available for free and has no upstream donation links.
- Search maintained store descriptions and metadata for old donation copy, old developer payment links, and claims that features unlock through donations. Replace with accurate getabreak copy.
- Do not add getabreak donation links, payment details, monetization, ads, account requirements, or tracking.
- Do not remove unrelated legitimate feature descriptions.
- If a premium-related screen is still used as a free-unlock or informational surface, preserve its functional behavior and only correct its branding/copy as necessary.

### 4. Update store metadata and public project copy

Audit existing store metadata, especially:

- `fastlane/metadata/android/en-US/full_description.txt`
- `fastlane/metadata/android/en-US/short_description.txt`
- Other existing locale metadata files, if present

Correct verified occurrences of old product branding and outdated donation instructions. Use concise, accurate language based on features already implemented in the repository. Do not invent capabilities, platform support, privacy guarantees, or setup requirements. Preserve accurate technical limitations and instructions.

Update the README's product name and project-owned links where necessary. Keep clear, accurate upstream attribution to DetoxDroid and retain the GPL-3.0 licence.

### 5. Update Gradle project display name

If the root project name is still `DetoxDroid 2` or otherwise refers to the previous product, change it to `getabreak` in `settings.gradle.kts`. Do not rename modules or alter build configuration unrelated to the display name.

### 6. Check system-visible Android labels

Inspect `AndroidManifest.xml` and relevant resources for labels/descriptions Android may display outside the app, including:

- Device-admin receiver label
- Accessibility service label/description
- Foreground-service subtype explanation
- Quick Settings tile labels
- Launcher activity label
- Widget labels/configuration labels

Change verified user-facing product references to `getabreak`. Preserve technical component class names, permissions, intent filters, metadata keys, and behavior.

### 7. Check image and launcher assets carefully

Inspect existing icon, splash, notification, and store artwork. If an asset visually contains the old product name, update only that asset as required and keep the current approved design.

- Do not create a new logo.
- Do not replace the approved symbol with generated artwork.
- Do not modify assets that do not contain old branding and are otherwise correct.
- Maintain Android adaptive-icon, monochrome-icon, density, and notification-icon requirements.
- Do not claim assets were visually inspected unless you actually opened or otherwise inspected them.

## Validation

Run a focused search after editing for likely old product-brand references, including:

- `Break`
- `DetoxDroid`
- `Detox Droid`
- `flxapps`
- `github.com/flxapps/DetoxDroid`
- `LiberaPay`
- `Ko-fi`
- `tip jar`
- `donate`
- `donation`

Review every match in context. Some matches are expected and must remain, including technical identifiers, upstream attribution, licence notices, feature wording, and external dependency references. Do not mechanically erase these.

If the environment supports Android builds, run:

```bash
./gradlew testFossDebugUnitTest
./gradlew assembleFossDebug
```

If either command fails, report the actual error and do not claim success. If Gradle cannot run because of environment/network/toolchain limitations, state that plainly.

If the project has CI configured, inspect the workflow trigger branches. Correct the PR-check workflow to include `main` only if that is the actual default/target branch and this can be changed without unrelated workflow redesign. Do not silently alter release/publishing workflows.

Finally inspect `git diff --check` and `git diff`. Revert any out-of-scope edits.

## Completion report

When finished, report:

1. The exact files changed and why.
2. The confirmed old-branding/funding issues fixed.
3. Any old-name search matches intentionally retained, with a short reason.
4. The exact build/test commands run and their actual results.
5. Any remaining issues that could not be verified or fixed.

Do not state “everything is fixed” unless the source review and validation support that conclusion.
