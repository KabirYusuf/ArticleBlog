#!/bin/bash

if [ -z "$CHANGE_TARGET" ] || [ -z "$GIT_BRANCH" ]; then
    echo "Required environment variables are not set."
    exit 1
fi


checkConventionalCommits() {
    pattern="^(feat|fix|docs|style|refactor|perf|test|chore|build|config)(\(.+\))?!?:\s.+"
    commitsList=$(git rev-list origin/${CHANGE_TARGET}..HEAD)

    for commit in $commitsList; do
        message=$(git log -1 --pretty=format:%s ${commit})
        if ! [[ $message =~ $pattern ]]; then
            echo "Commit ${commit} does not follow Conventional Commits format: ${message}"
            exit 1
        fi
    done
}


checkCommitAuthors() {
    emailPattern=".+@internetbrands.com$"
    commitsList=$(git rev-list origin/${CHANGE_TARGET}..HEAD)

    for commit in $commitsList; do
        authorEmail=$(git log -1 --pretty=format:%ae ${commit})
        if ! [[ $authorEmail =~ $emailPattern ]]; then
            echo "Commit ${commit} has an author with non-Internet Brands email: ${authorEmail}"
            exit 1
        fi
    done
}


checkBranchNamingConvention() {
    branchPattern="^(feature)/.+"
    if ! [[ $GIT_BRANCH =~ $branchPattern ]]; then
        echo "Branch name ${GIT_BRANCH} does not follow the conventional naming convention"
        exit 1
    fi
}


checkConventionalCommits
checkCommitAuthors
checkBranchNamingConvention
