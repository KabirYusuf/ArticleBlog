import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import ViewAllArticles from '@/views/ViewAllArticles.vue';
import ArticleView from '@/views/ArticleView.vue';
import MyProfile from '@/views/MyProfile.vue'
import EditProfile from '@/views/EditProfile.vue'
import AddArticle from '@/views/AddArticle.vue'
import EditArticle from '@/views/EditArticle.vue'
import { useUserStore } from "@/store/userStore";
import OauthSuccess from '@/views/OauthSuccess.vue';


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "home",
            component: HomeView,
            meta: { isUserVerified: true }
        },

        {
            path: '/view-all-articles',
            name: 'viewAllArticles',
            component: ViewAllArticles,
            meta: { isUserVerified: true }
        },
        {
            path: '/article/:id',
            name: 'article',
            component: ArticleView,
            meta: { isUserVerified: true }
          },
          {
            path: '/my-profile',
            name: 'my-profile',
            component: MyProfile,
            meta: { requiresAuth: true, isUserVerified: true }
          },
          {
            path: '/edit-profile',
            name: 'edit-profile',
            component: EditProfile,
            meta: { requiresAuth: true, isUserVerified: true }
          },
          {
            path: '/add-new-article',
            name: 'add-new-article',
            component: AddArticle,
            meta: { requiresAuth: true, isUserVerified: true }
          },
          {
            path: '/edit-article/:id',
            name: 'edit-article',
            component: EditArticle,
            meta: { requiresAuth: true, isUserVerified: true }
          },
          {
            path: '/oauth-success',
            name: 'OauthSuccess',
            component: OauthSuccess,
        },
    ],
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();

  const isUserVerified = to.matched.some(record => record.meta.isUserVerified);
  if (isUserVerified && !userStore.isUserVerified) {
      next()
  }

  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

  if (requiresAuth && !userStore.isLoggedIn) {
      next({ name: 'home' });
  } else {
      next();
  }
});

export default router;
