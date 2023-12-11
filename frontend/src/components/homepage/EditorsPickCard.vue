<template>
    <div class="editorsCard">
        <img :src="imageUrl" alt="Editor's Pick Image" class="editorsCard__image">
        <ul class="editorsCard__inner editorsCard_tagList">
            <li class="editorsCard__tagListItem editorsCard__tagListItem--pushRight">
                {{ card?.tag }}
            </li>
        </ul>

        <i :class="[icon, 'editorsCard__icon']"></i>

        <div @click="handleArticleEdit" v-if="isProfilePage" class="editorsCard__editIcon">
            <Pencil fill="#000" />
        </div>
        <div class="editorsCard__inner editorsCard__writeUp">
            <time class="editorsCard__time">{{ card?.createdAt }}</time>
            <h3 class="editorsCard__title">{{ card?.title }}</h3>
            <p class="editorsCard__para">{{ card?.content }}</p>
        </div>
    </div>
</template>

<script setup>
import { Pencil } from 'lucide-vue-next';
import defaultAvatar from '@/assets/image.jpg';
import { computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const props = defineProps(
    ['card', 'isProfilePage']
)
const imageUrl = computed(() => {
    return props.card?.image || defaultAvatar;
});

const handleArticleEdit = () => {
    router.push({ name: 'edit-article', params: { id: props.card?.id } });
};

</script>