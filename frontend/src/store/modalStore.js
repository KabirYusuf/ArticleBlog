import { defineStore } from "pinia";

export const useModalStore = defineStore("modal", {
    state: () => ({
        showLogin: false,
        showRegister: false,
    }),
    actions: {
        openModal(value) {
            if (value === 'login') {
                this.showLogin = true;
            }
            if (value === 'register') {
                this.showRegister = true;
            }
        },
        closeModal() {
            this.showLogin = false;
            this.showRegister = false;
        },
    },
});
