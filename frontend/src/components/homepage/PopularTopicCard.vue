<template>
    <div class="cardContainer" @click="handleClick">
        <ul class="popularTopicCard__tagList">
                <li class="popularTopicCard__tagListItem popularTopicCard__tagListItem--pushRight">
                    {{ cardTagOne }}
                </li>
                <li class="popularTopicCard__tagListItem">
                    {{ cardTagTwo }}
                </li>
        </ul>
        <!-- <img :src="cardImage" alt="Popular Topic Image" class="card__image"> -->
        <img src="../../../public/card_images/BlogImage3.jpg" alt="image" class="card__image">
        <div class="card__info">
            <div class="cardTimeAndIcon">
                <time class="card__time">{{ cardTime }}</time>
                <i :class="[icon, 'card__icon']"></i>
            </div>
            <h3 class="card__title">{{ cardTitle }}</h3>
            <p class="card__para">{{ cardPara }}</p>

            <div class="card__author">
                <!-- <img v-if="cardAuthorImage" :src="cardAuthorImage" alt="Author image" class="cardAuthor__image"> -->
                <img src="../../../public/Image.jpg" alt="Author image" class="cardAuthor__image">
                <div class="cardAuthor">
                    <p v-if="cardAuthorName" class="cardAuthor__name">{{ cardAuthorName }}</p>
                    <p v-if="cardAuthorProfession" class="cardAcardAuthor--profession">{{ cardAuthorProfession }}</p>
                </div>
            </div>

        </div>
    
    </div>
</template>

<script setup>
import {defineEmits} from 'vue';
const emit = defineEmits(['clickCard']);
    const props = defineProps(
        ['cardTitle', 'cardImage', 'cardPara', 'cardAuthorImage', 
        'cardAuthorName', 'cardAuthorProfession', 'cardTime', 'cardTagOne','cardTagTwo', 'icon', 'id']
    )

    const handleClick = () => {
        emit('clickCard', props.id);
    };

</script>