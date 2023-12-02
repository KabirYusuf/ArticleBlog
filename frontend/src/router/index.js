import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import ViewAllArticles from '@/views/ViewAllArticles.vue';
import ArticleView from '@/views/ArticleView.vue';
import MyProfile from '@/views/MyProfile.vue'
import EditProfile from '@/views/EditProfile.vue'
import AddArticle from '@/views/AddArticle.vue'
import EditArticle from '@/views/EditArticle.vue'


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "home",
            component: HomeView,
        },

        {
            path: '/view-all-articles',
            name: 'viewAllArticles', 
            component: ViewAllArticles,
        },
        {
            path: '/article/:id',
            name: 'article',
            component: ArticleView,
          },
          {
            path: '/my-profile',
            name: 'my-profile',
            component: MyProfile,
          },
          {
            path: '/edit-profile',
            name: 'edit-profile',
            component: EditProfile,
          },
          {
            path: '/add-new-article',
            name: 'add-new-article',
            component: AddArticle,
          },
          {
            path: '/edit-article/:id',
            name: 'edit-article',
            component: EditArticle,
          },
    ],
});

export default router;
