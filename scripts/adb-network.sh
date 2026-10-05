#!/usr/bin/env bash
set -euo pipefail

SERIAL="${ANDROID_SERIAL:-}"
ADB=(adb)
if [[ -n "$SERIAL" ]]; then
  ADB=(adb -s "$SERIAL")
fi

usage() {
  echo "Usage: $0 <wifi-on|wifi-off|data-on|data-off|airplane-on|airplane-off|net-on|net-off>" >&2
  exit 1
}

[[ $# -eq 1 ]] || usage

case "$1" in
  wifi-on) "${ADB[@]}" shell svc wifi enable ;;
  wifi-off) "${ADB[@]}" shell svc wifi disable ;;
  data-on) "${ADB[@]}" shell svc data enable ;;
  data-off) "${ADB[@]}" shell svc data disable ;;
  airplane-on)
    "${ADB[@]}" shell settings put global airplane_mode_on 1
    "${ADB[@]}" shell am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true
    ;;
  airplane-off)
    "${ADB[@]}" shell settings put global airplane_mode_on 0
    "${ADB[@]}" shell am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false
    ;;
  net-off)
    "${ADB[@]}" shell svc wifi disable
    "${ADB[@]}" shell svc data disable
    ;;
  net-on)
    "${ADB[@]}" shell settings put global airplane_mode_on 0
    "${ADB[@]}" shell svc wifi enable
    "${ADB[@]}" shell svc data enable
    ;;
  *) usage ;;
esac
