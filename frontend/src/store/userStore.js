import { defineStore } from 'pinia';
import { getUser } from '../utility/userApiService';

export const useUserStore = defineStore('user', {
    state: () => ({
        isLoggedIn: !!localStorage.getItem('token'),
        isUserVerified: null,
        user: null,
    }),
    actions: {
        async fetchUser() {
            try {
                const response = await getUser();
                this.user = response;
                this.isLoggedIn = true;
            } catch (error) {
                console.error(error.message);
                this.user = null;
                this.isLoggedIn = false;
            }
        },
        logIn() {
            this.isLoggedIn = true;

        },
        async isUserVerified() {
            const response = await getUser();
            if (response?.verified) {
                this.isUserVerified = true;
            }

            this.logOut()
            this.isUserVerified = false;
        },
        logOut() {
            this.isLoggedIn = false;
            this.user = null;
            localStorage.removeItem('token');
        },
    },
});

