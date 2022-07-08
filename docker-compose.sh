#!/bin/sh

MAIN_MODULE_IMAGE_ID=$(docker images -aq spring-shopping-api_shopping-api)
if [[ ! -z "${MAIN_MODULE_IMAGE_ID}" ]]; then
  echo "Removing previously MAIN_MODULE_IMAGE with id: ${MAIN_MODULE_IMAGE_ID}"
  docker rm $(docker rmi -f ${MAIN_MODULE_IMAGE_ID})
fi

MAIN_MODULE_IMAGE_ID=$(docker images -aq spring-shopping-api_prometheus)
if [[ ! -z "${MAIN_MODULE_IMAGE_ID}" ]]; then
  echo "Removing previously MAIN_MODULE_IMAGE with id: ${MAIN_MODULE_IMAGE_ID}"
  docker rm $(docker rmi -f ${MAIN_MODULE_IMAGE_ID})
fi

MAIN_MODULE_IMAGE_ID=$(docker images -aq spring-shopping-api_service-discovery)
if [[ ! -z "${MAIN_MODULE_IMAGE_ID}" ]]; then
  echo "Removing previously MAIN_MODULE_IMAGE with id: ${MAIN_MODULE_IMAGE_ID}"
  docker rm $(docker rmi -f ${MAIN_MODULE_IMAGE_ID})
fi

MAIN_MODULE_IMAGE_ID=$(docker images -aq spring-shopping-api_gateway)
if [[ ! -z "${MAIN_MODULE_IMAGE_ID}" ]]; then
  echo "Removing previously MAIN_MODULE_IMAGE with id: ${MAIN_MODULE_IMAGE_ID}"
  docker rm $(docker rmi -f ${MAIN_MODULE_IMAGE_ID})
fi

echo "Building project"
./gradlew clean build

echo "Starting Docker Compose"
#docker-compose up --build --force-recreate shopping-api service-discovery database gateway
docker-compose up --build --force-recreate