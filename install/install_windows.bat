@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"

if not exist "platform-tools\adb.exe" (
    echo Downloading and unzipping Android Platform Tools. Please wait...
    powershell.exe -nologo -noprofile -command "(New-Object Net.WebClient).DownloadFile('https://dl.google.com/android/repository/platform-tools-latest-windows.zip', 'platform-tools-latest-windows.zip')"
    powershell.exe -nologo -noprofile -command "& { $shell = New-Object -COM Shell.Application; $target = $shell.NameSpace('%cd%'); $zip = $shell.NameSpace('%cd%\platform-tools-latest-windows.zip'); $target.CopyHere($zip.Items(), 16); }"
)

if not exist "platform-tools\adb.exe" (
    echo Error: platform-tools\adb.exe was not found after setup.
    exit /b 1
)

set "APK_FILE="
set "APK_URL="

for %%f in (*.apk) do (
    if "!APK_FILE!"=="" (
        set "APK_FILE=%%f"
        echo Using local APK: !APK_FILE!
    )
)

if "!APK_FILE!"=="" (
    echo Checking for latest release on GitHub...
    for /f "usebackq tokens=1,2 delims=|" %%i in (`powershell.exe -nologo -noprofile -command "try { $release = Invoke-RestMethod 'https://api.github.com/repos/mizan989/getabreak-focus_app/releases/latest'; $asset = $release.assets | Where-Object { $_.name -match '.*\.apk$' } | Select-Object -First 1; if ($null -ne $asset) { Write-Output ($asset.browser_download_url + '|' + $asset.name) } } catch {}"`) do (
        set "APK_URL=%%i"
        set "APK_FILE=%%j"
    )
    if not "!APK_URL!"=="" (
        echo Downloading latest getabreak APK (!APK_FILE!)...
        powershell.exe -nologo -noprofile -command "(New-Object Net.WebClient).DownloadFile('!APK_URL!', '!APK_FILE!')"
    )
)

if "!APK_FILE!"=="" (
    echo.
    echo Error: No getabreak APK was found.
    echo Either:
    echo   1. Copy a getabreak APK file (*.apk) into this directory, or
    echo   2. Publish a release with an attached APK asset at:
    echo      https://github.com/mizan989/getabreak-focus_app/releases
    echo.
    pause
    exit /b 1
)

if not exist "!APK_FILE!" (
    echo Error: Expected APK file "!APK_FILE!" was not found.
    pause
    exit /b 1
)

echo Starting ADB
echo If your device asks you to allow USB Debugging, press "Accept".
%cd%\platform-tools\adb.exe start-server

echo Installing getabreak on your device...
%cd%\platform-tools\adb.exe install -r -t "!APK_FILE!"

echo Granting Permissions
%cd%\platform-tools\adb.exe shell pm grant com.getabreak.app android.permission.WRITE_SECURE_SETTINGS
%cd%\platform-tools\adb.exe shell "dpm set-device-owner com.getabreak.app/.system_integration.DetoxDroidDeviceAdminReceiver"

echo Starting App
%cd%\platform-tools\adb.exe shell monkey -p com.getabreak.app 1

echo Done.
pause
