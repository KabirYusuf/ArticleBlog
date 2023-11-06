
<script setup>
    import PopularTopicCard from '../components/PopularTopicCard.vue'
    import EditorsPickCard from '../components/EditorsPickCard.vue'
</script>

<template>
    <main class="home">
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
        
                <PopularTopicCard cardTitle="Dream destinations to visit this year in Paris" cardImage="../../public/card_images/Blog Image.jpg"
                                    cardPara="Progressively incentivize cooperative systems through technically
                            sound functionalities. The credibly productivate seamless data." cardAuthorImage="../../public/author_images/Ellipse 4.jpg"
                            cardAuthorName="By jennifer Lawrence"  cardAuthorProfession="Thinker & Designer" cardTime="08.08.2021"
                            cardTagOne="ADVENTURE" icon="fas fa-gem"
                />

                <PopularTopicCard cardTitle="Richird Norton photorealistic rendering as real photos" cardImage="../../public/card_images/BlogImage 2.jpg"
                                    cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data." cardAuthorImage="../../public/author_images/Ellipse2.jpg"
                            cardAuthorName="By David Arthur"  cardAuthorProfession="Designer" cardTime="08.08.2021"
                            cardTagOne="BRAND" cardTagTwo="FASHION" icon="fas fa-gem"
                />

                <PopularTopicCard cardTitle="Dream destinations to visit this year in Paris" cardImage="../../public/card_images/BlogImage3.jpg"
                                    cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data." cardAuthorImage="../../public/author_images/Ellipse3.jpg"
                            cardAuthorName="By Sarah Lawrence"  cardAuthorProfession="Journalist" cardTime="08.08.2021"
                            cardTagOne="ADVENTURE"
                />
                <PopularTopicCard cardTitle="Dream destinations to visit this year in Paris" cardImage="../../public/card_images/BlogImage4.jpg"
                                    cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data." cardAuthorImage="../../public/author_images/Ellipse 4.jpg"
                            cardAuthorName="By Jennifer Lawrence"  cardAuthorProfession="Thinker & Designer" cardTime="08.08.2021"
                            cardTagOne="ADVENTURE"
                />
                
                <PopularTopicCard cardTitle="Art of seasons: 40+ Bright Illustrations by Nature" cardImage="../../public/card_images/BlogImage5.jpg"
                                    cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data." cardAuthorImage="../../public/author_images/Ellipse5.jpg"
                            cardAuthorName="By David Tomas"  cardAuthorProfession="Thinker" cardTime="08.08.2021"
                />

                <PopularTopicCard cardTitle="The Anatomy of a web page and Basics Elements" cardImage="../../public/card_images/BlogImage6.jpg"
                                    cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data." cardAuthorImage="../../public/author_images/Ellipse 4.jpg"
                            cardAuthorName="By Jennifer Lawrence"  cardAuthorProfession="Thinker & Designer" cardTime="08.08.2021"
                />

                <PopularTopicCard cardTitle="Types of Contrast in User Interface Design" cardImage="../../public/card_images/BlogImage7.jpg"
                                    cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data." cardAuthorImage="../../public/author_images/Ellipse7.jpg"
                            cardAuthorName="By Andrey Edison"  cardAuthorProfession="Thinker & Designer" cardTime="08.08.2021"
                />

                <PopularTopicCard cardTitle="Dream destinations to visit this year in Paris" cardImage="../../public/card_images/BlogImage8.jpg"
                                    cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data." cardAuthorImage="../../public/author_images/Ellipse8.jpg"
                            cardAuthorName="By Sean Anderson"  cardAuthorProfession="Thinker & Designer" cardTime="08.08.2021"
                />
                            
            </div>
        </section>

        <section class="editorsPick">
            <h3 class="editorsPick__heading">Editor's Pick</h3>
            <div class="editorsPick__card">
                <EditorsPickCard cardImage="../../public/editors_images/Editor1.jpg" cardTime="08.08.2021"
                            cardTag="FASHION" cardTitle="Richird Norton photorealistic rendering as real photos"
                            cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data" icon="fas fa-gem"
                />
                <EditorsPickCard cardImage="../../public/editors_images/Editor2.jpg" cardTime="08.08.2021"
                            cardTag="FASHION" cardTitle="Richird Norton photorealistic rendering as real photos"
                            cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data"
                />
                <EditorsPickCard cardImage="../../public/editors_images/Editor3.jpg" cardTime="08.08.2021"
                            cardTag="FASHION" cardTitle="Richird Norton photorealistic rendering as real photos"
                            cardPara="Progressively incentivize cooperative systems through technically 
                            sound functionalities. The credibly productivate seamless data"
                />
            </div>
            
        </section>
        
        
    </main>
</template>
