<template>
    <ProfileHeader>
        <div class="myProfileHeader__content">
            <div class="myProfileHeader__inner">
                <img :src="userImageUrl" alt="profile-image" class="profileHeader__image">
                <h2 class="profile__name" v-if="fullName">{{ fullName }}</h2>
                <h2 class="profile__name" v-else>{{ userStore.user?.username }}</h2>
                <p class="profile__email">{{ userStore.user?.email }}</p>
                <router-link to="/edit-profile" class="profile__link">Edit profile</router-link>
                <router-link to="/my-profile" class="profile__link">Manage subscription</router-link>
            </div>
        </div>
    </ProfileHeader>

    <section class="userArticle__container">
        <h2 class="userArticle__heading">My articles</h2>

        <div class="userArticle__article">
            <router-link to="/add-new-article" class="userArticle__link">
                <div class="userArticle__addArticleCard">
                    <div class="userArticle__iconAndText">
                        <PlusCircle />
                        <p>Add new article</p>
                    </div>
                </div>
            </router-link>
            <EditorsPickCard v-for="(userArticle, index) in userArticles" :key="index" :card="userArticle"
                :isProfilePage="true" />
        </div>
    </section>

    <section class="subcription__container">
        <div class="subscriptionHeading__inner">
            <h2 class="subscription__title">My subscription</h2>
            <p class="subscription__addNew">Add subscription</p>
        </div>

        <div class="subscription__innerContent">
            <div class="subscription__status">
                <p class="subscription__state">No active subscription</p>
                <Button class="subscription__button">Go Premium</Button>
            </div>
        </div>
    </section>

    <Footer :isConnection="true" />
</template>

<script setup>
import ProfileHeader from '../components/layouts/ProfileHeader.vue'
import EditorsPickCard from '../components/homepage/EditorsPickCard.vue';
import { ref, onMounted, computed } from 'vue';
import { PlusCircle } from 'lucide-vue-next';
import Footer from '../components/layouts/Footer.vue';
import Button from '../components/inputs/Button.vue';
import { useUserStore } from '../store/userStore';
import { getAllArticles } from '../utility/articleApiService'


const userStore = useUserStore();

const rawArticles = ref([]);


onMounted(async () => {
    await userStore.fetchUser();
    try {
        const articlesResponse = await getAllArticles();
        rawArticles.value = articlesResponse._embedded.items.sort((a, b) => a.id - b.id);
    } catch (error) {
        console.error('Error fetching articles:', error);
    }
});

const fullName = computed(() => {
    if (userStore.user && (userStore.user.firstName || userStore.user.lastName)) {
        return [userStore.user.firstName, userStore.user.lastName].filter(Boolean).join(' ');
    }
    return '';
});

const userArticles = computed(() => {
    if (userStore.user && rawArticles.value.length) {
        return rawArticles.value.filter(article => article.user.id === userStore.user.id);
    }
    return [];
});

const userImageUrl = computed(() => {
    return userStore.user?.userImage || '../../public/profile-avatar.webp';
});

</script>