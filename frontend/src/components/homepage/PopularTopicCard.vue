<template>
    <div class="cardContainer" @click="handleclickOfArticle">
        <ul class="popularTopicCard__tagList">
            <li class="popularTopicCard__tagListItem popularTopicCard__tagListItem--pushRight">
                <!-- {{ cardTagOne }} -->
            </li>
            <li class="popularTopicCard__tagListItem">
                <!-- {{ cardTagTwo }} -->
            </li>
        </ul>
        <img src="../../../public/card_images/BlogImage3.jpg" alt="image" class="card__image">
        <div class="card__info">
            <div class="cardTimeAndIcon">
                <time class="card__time">{{ card?.createdAt }}</time>
                <i :class="[icon, 'card__icon']"></i>
            </div>
            <h3 class="card__title">{{ card?.title }}</h3>
            <p class="card__para">{{ card?.content }}</p>

            <div class="card__author">
                <img src="../../../public/Image.jpg" alt="Author image" class="cardAuthor__image">
                <div class="cardAuthor">
                    <p class="cardAuthor__name">{{ card?.user.firstName }} {{ card?.user.lastName }}</p>
                    <p v-if="cardAuthorProfession" class="cardAcardAuthor--profession">Journalist</p>
                </div>
            </div>

        </div>

    </div>
</template>

<script setup>


import { useRouter } from 'vue-router';
import { handleArticleClick } from '../../utility/articleLogic'


const router = useRouter();

const props = defineProps(
    ['card']
)


const handleclickOfArticle = () => {
    handleArticleClick(props.card.id, router)
}

</script>