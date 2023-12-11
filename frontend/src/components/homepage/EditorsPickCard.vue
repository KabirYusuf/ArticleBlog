<template>
    <div class="editorsCard">
        <img :src="card?.image" alt="Editor's Pick Image" class="editorsCard__image">
        <ul class="editorsCard__inner editorsCard_tagList">
                <li class="editorsCard__tagListItem editorsCard__tagListItem--pushRight">
                    {{ card?.tag }}
                </li>
        </ul>
        <i :class="[icon, 'editorsCard__icon']"></i>
        
        <div class="editorsCard__inner editorsCard__writeUp">
            <time class="editorsCard__time">{{ card?.createdAt}}</time>
            <h3 class="editorsCard__title">{{ card?.title}}</h3>
            <p class="editorsCard__para">{{ card?.content }}</p>
        </div>
    </div>
</template>

<script setup>
    defineProps(
        ['card']
    )
</script>