@Library('jenkins-build-helpers') _
setupEnvironment(['business_unit': 'corp'])

def createTestingEnvironment() {
    return setupContainers([
        [
            'name': 'main',
            'image': 'ike-docker-local.artifactory.internetbrands.com/corp/levelup-academy:kyusuf-testing-image',
            'imagePullPolicy': 'Always',
            'env': [
                ['name': 'DB_HOST', 'value': 'localhost'],
                ['name': 'PGPASSWORD', 'value': 'password'],
                ['name': 'DB_DATABASE', 'value': 'levelup'],
                ['name': 'DB_USERNAME', 'value': 'levelup'],
                ['name': 'DB_PASSWORD', 'value': 'password'],
                ['name': 'LOG_CHANNEL', 'value': 'single'],
                ['name': 'LOG_LEVEL', 'value': 'debug']
            ]
        ],
        [
            'name': 'pgsql',
            'image': 'postgres:14',
            'env': [
                ['name': 'PGPASSWORD', 'value': 'password'],
                ['name': 'POSTGRES_DB', 'value': 'levelup'],
                ['name': 'POSTGRES_USER', 'value': 'levelup'],
                ['name': 'POSTGRES_PASSWORD', 'value': 'password']
            ]
        ]
    ])
}

pipeline {
    agent none

    options {
        gitLabConnection('IB Gitlab')
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    if (env.CHANGE_ID) {
                        currentBuild.description = 'MR changed event'
                    } else {
                        currentBuild.description = 'Branch push event'
                    }
                }
            }
        }

        stage('Build pipeline testing image') {
            when {
                expression { env.CHANGE_ID == null }
            }
            agent {
                kubernetes {
                    yaml dockerContainerImageBuildAndPushPodManifest()
                }
            }
            steps {
                container('builder') {
                    dockerContainerImageBuildAndPush([
                        'docker_repo_host': 'ike-docker-local.artifactory.internetbrands.com',
                        'docker_repo_credential_id': 'artifactory-ike',
                        'dockerfile': './ci/Dockerfile',
                        'docker_image_name': 'levelup-academy',
                        'docker_image_tag': 'kyusuf-testing-image'
                    ])
                }
            }
        }

        stage('Conventional Commits and Author Check') {
            when {
                expression { env.CHANGE_ID != null }
            }
            agent {
                kubernetes {
                    yaml createTestingEnvironment()
                }
            }
            steps {
                container('main') {
                    echo 'MR event detected'
                    sh "CHANGE_TARGET=${env.CHANGE_TARGET} GIT_BRANCH=${env.GIT_BRANCH} ./ci/do-mr-checks.sh"
                }
            }
        }

        stage('Run backend tests') {
            when {
                expression { env.CHANGE_ID == null }
            }
            agent {
                kubernetes {
                    yaml createTestingEnvironment()
                }
            }
            steps {
                container('main') {
                    echo 'Push event detected'
                    echo 'Checking if Docker is installed...'
                    sh 'docker --version || echo "Docker is not installed"'
                    echo 'Running a simple Docker command...'
                    sh 'docker run hello-world || echo "Docker command failed"'
                    sh './ci/do-checks.sh'
                }
            }
            post {
                success {
                    updateGitlabCommitStatus name: 'backend-tests', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: 'backend-tests', state: 'failed'
                }
            }
        }
    }
}

