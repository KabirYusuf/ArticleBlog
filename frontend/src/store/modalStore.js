import { defineStore } from "pinia";

export const useModalStore = defineStore("modal", {
    state: () => ({
        showLogin: false,
        showRegister: false,
        showEmailVerification: false,
    }),
    actions: {
        openModal(value) {
            if (value === 'login') {
                this.showLogin = true;
            }
            if (value === 'register') {
                this.showRegister = true;
            }
            if (value === 'emailVerification') {
                this.showEmailVerification = true;
            }
        },
        closeModal() {
            this.showLogin = false;
            this.showRegister = false;
        },

        closeVerificationModal() {
            this.showEmailVerification = false;
        }
    },
});
