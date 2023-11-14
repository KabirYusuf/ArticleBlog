<template>
    <div class="editorsCard">
        <img :src="cardImage" alt="Editor's Pick Image" class="editorsCard__image">
        <ul class="editorsCard__inner editorsCard_tagList">
                <li class="editorsCard__tagListItem editorsCard__tagListItem--pushRight">
                    {{ cardTag }}
                </li>
        </ul>
        <i :class="[icon, 'editorsCard__icon']"></i>
        
        <div class="editorsCard__inner editorsCard__writeUp">
            <time class="editorsCard__time">{{ cardTime }}</time>
            <h3 class="editorsCard__title">{{ cardTitle }}</h3>
            <p class="editorsCard__para">{{ cardPara }}</p>
        </div>
    </div>
</template>

<script setup>
    defineProps(
        ['cardImage', 'cardTitle', 'cardPara', 'cardTime','cardTag', 'icon']
    )
</script>