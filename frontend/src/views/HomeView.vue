<script setup>
import { ref } from 'vue';
import PopularTopicCard from '@/components/homepage/PopularTopicCard.vue';
import EditorsPickCard from '@/components/homepage/EditorsPickCard.vue';

const cards = ref([
  {
    cardTitle: "Dream destinations to visit this year in Paris",
    cardImage: "card_images/Blog Image.jpg",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
    cardAuthorImage: "author_images/Ellipse 4.jpg",
    cardAuthorName: "By Jennifer Lawrence",
    cardAuthorProfession: "Thinker & Designer",
    cardTime: "08.08.2021",
    cardTagOne: "ADVENTURE",
    icon: "fas fa-gem"
  },
  {
    cardTitle: "Dream destinations to visit this year in Paris",
    cardImage: "card_images/BlogImage 2.jpg",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
    cardAuthorImage: "author_images/Ellipse2.jpg",
    cardAuthorName: "By David Arthur",
    cardAuthorProfession: "Designer",
    cardTime: "08.08.2021",
    cardTagOne: "BRAND",
    cardTagTwo: "FASHION",
    icon: "fas fa-gem"
  },
  {
    cardTitle: "Dream destinations to visit this year in Paris",
    cardImage: "card_images/BlogImage3.jpg",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
    cardAuthorImage: "author_images/Ellipse3.jpg",
    cardAuthorName: "By Sarah Lawrence",
    cardAuthorProfession: "Journalist",
    cardTime: "08.08.2021",
    cardTagOne: "ADVENTURE",
  },
  {
    cardTitle: "Dream destinations to visit this year in Paris",
    cardImage: "card_images/BlogImage4.jpg",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
    cardAuthorImage: "author_images/Ellipse 4.jpg",
    cardAuthorName: "By Jennifer Lawrence",
    cardAuthorProfession: "Thinker & Designer",
    cardTime: "08.08.2021",
    cardTagOne: "ADVENTURE",
  },
  {
    cardTitle: "Art of seasons: 40+ Bright Illustrations by Nature",
    cardImage: "card_images/BlogImage5.jpg",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
    cardAuthorImage: "author_images/Ellipse5.jpg",
    cardAuthorName: "By David Tomas",
    cardAuthorProfession: "Thinker",
    cardTime: "08.08.2021",
  },
  {
    cardTitle: "The Anatomy of a web page and Basics Elements",
    cardImage: "card_images/BlogImage6.jpg",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
    cardAuthorImage: "author_images/Ellipse 4.jpg",
    cardAuthorName: "By Jennifer Lawrence",
    cardAuthorProfession: "Thinker & Designer",
    cardTime: "08.08.2021",
  },
  {
    cardTitle: "Types of Contrast in User Interface Design",
    cardImage: "card_images/BlogImage7.jpg",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
    cardAuthorImage: "author_images/Ellipse7.jpg",
    cardAuthorName: "By Andrey Edison",
    cardAuthorProfession: "Thinker & Designer",
    cardTime: "08.08.2021",
  },
  {
    cardTitle: "Dream destinations to visit this year in Paris",
    cardImage: "card_images/BlogImage8.jpg",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data.",
    cardAuthorImage: "author_images/Ellipse8.jpg",
    cardAuthorName: "By Sean Anderson",
    cardAuthorProfession: "Thinker & Designer",
    cardTime: "08.08.2021",
  },
]);

const editorsPicks = ref([
  {
    cardImage: "editors_images/Editor1.jpg",
    cardTime: "08.08.2021",
    cardTag: "FASHION",
    cardTitle: "Richard Norton photorealistic rendering as real photos",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
    icon: "fas fa-gem"
  },
  {
    cardImage: "editors_images/Editor2.jpg",
    cardTime: "08.08.2021",
    cardTag: "FASHION",
    cardTitle: "Richard Norton photorealistic rendering as real photos",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
  },
  {
    cardImage: "editors_images/Editor3.jpg",
    cardTime: "08.08.2021",
    cardTag: "FASHION",
    cardTitle: "Richard Norton photorealistic rendering as real photos",
    cardPara: "Progressively incentivize cooperative systems through technically sound functionalities. The credibly productivate seamless data",
  },
]);

</script>


<template>
        <section class="popularTopics">
            <h3 class="popularTopics__heading">Popular topics</h3>
            <ul class="popularTopics__list">
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">All</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Adventure</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Travel</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Fashion</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Technology</a></li>
                <li class="popularTopics__listItem"><a href="#" class="popularTopics__listLink">Branding</a></li>
                <li class="popularTopics__listItem popularTopics__listItem--right" ><a href="#" class="popularTopics__listLink">View All</a></li>
            </ul>
            <div class="popularTopics__card">
        
                <PopularTopicCard
                    v-for="(card, index) in cards"
                    :key="index"
                    :cardTitle="card.cardTitle"
                    :cardImage="card.cardImage"
                    :cardPara="card.cardPara"
                    :cardAuthorImage="card.cardAuthorImage"
                    :cardAuthorName="card.cardAuthorName"
                    :cardAuthorProfession="card.cardAuthorProfession"
                    :cardTime="card.cardTime"
                    :cardTagOne="card.cardTagOne"
                    :cardTagTwo="card.cardTagTwo"
                    :icon="card.icon"
                />
                            
            </div>
        </section>

        <section class="editorsPick">
            <h3 class="editorsPick__heading">Editor's Pick</h3>
            <div class="editorsPick__card">
                <EditorsPickCard
                    v-for="(editorPick, index) in editorsPicks"
                    :key="index"
                    :cardImage="editorPick.cardImage"
                    :cardTime="editorPick.cardTime"
                    :cardTag="editorPick.cardTag"
                    :cardTitle="editorPick.cardTitle"
                    :cardPara="editorPick.cardPara"
                    :icon="editorPick.icon"
                />
            </div>
            
        </section>
</template>
