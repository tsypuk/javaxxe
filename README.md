[![Build Status](https://travis-ci.org/tsypuk/javaxxe.svg?branch=master)](https://travis-ci.org/tsypuk/javaxxe)
[![star this repo](http://githubbadges.com/star.svg?user=tsypuk&repo=javaxxe&style=default)](https://github.com/tsypuk/javaxxe)
[![fork this repo](http://githubbadges.com/fork.svg?user=tsypuk&repo=javaxxe&style=default)](https://github.com/tsypuk/javaxxe/fork)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## TODO ITEMS

- [x] create springboot app bootstrap
- [x] add xml file upload endpont
- [x] add xml parser service
- [x] add more styling to bootstrap
- [ ] create selenium automation test for hack action (all injections)
- [ ] create docker image and container with fake DDT server based on lighthttp
Use images like. https://github.com/spujadas/lighttpd-docker
- [ ] move docker configs into separate folder
- [x] create docker image for app with diff java version and artifact version
- [ ] automate penetration testing with docker and testcontainers(if needed)
- [x] check to use openjdk or oracle versions for testing
- [ ] update slides of presentation

## Ports mapping

| Name        |  JDK/JRE        | container port | machine port |
| ----------- | --------------- | -------------- | ------------ |
| simpson.jar | java9           | 8080           | future       |
| simpson.jar | java8           | 8080           | 8088         |
| simpson.jar | java7           | 8080           | 8087         |
