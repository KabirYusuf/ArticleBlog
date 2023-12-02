import { defineStore } from 'pinia';
import { getUser } from '../utility/userApiService';

export const useUserStore = defineStore('user', {
    state: () => ({
        isLoggedIn: !!localStorage.getItem('token'),
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
        logOut() {
            this.isLoggedIn = false;
            this.user = null;
            localStorage.removeItem('token');
        },
    },
});

