<template>
    <div class="social__content">
        <small class="social__title">Sign In with: </small>
        <p><a href="#" class="social__button" id="gitlab__connect" @click="handleGitlabAuth"> <span>Gitlab</span></a></p>
        <p><a href="#" class="social__button" id="google__connect" @click="handleGoogleAuth"> <span> Google</span></a></p>
    </div>
</template>

<script setup>
const handleGitlabAuth = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/gitlab';
};

const handleGoogleAuth = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
};

</script>
