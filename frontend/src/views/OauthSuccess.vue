<template>
    <div>Authenticating...</div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import {useUserStore} from '@/store/userStore'

onMounted(() => {
    const router = useRouter();
    const route = useRoute();
    const token = route.query.token;
    const userStore = useUserStore();

    if (token) {
        
        localStorage.setItem('token', token);
        userStore.logIn();
        
        
        router.push({ name: 'home' });
    } else {
        
        router.push({ name: 'home' });
    }
});
</script>
