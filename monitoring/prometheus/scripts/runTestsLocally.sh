#!/usr/bin/env bash
set +e

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
project_dir="${script_dir}/../.."
cd ${project_dir}

docker rmi prometheus-test
set -e

./gradlew --no-daemon --console plain :prometheus:testUsingDockerImage
