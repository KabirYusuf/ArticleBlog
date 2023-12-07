#!/bin/bash

echo 'Will run tests'

echo 'Checking maven version'
mvn --version

# For reference see: https://plugins.jenkins.io/gitlab-branch-source/#plugin-content-environment-variables
# (variables such as this will be useful for your homework).
echo "Branch $BRANCH_NAME going into $CHANGE_TARGET implemented by $CHANGE_AUTHOR"

cd backend
mvn test -e
