import { defineStore } from "pinia";

export const useModalStore = defineStore("modal", {
  state: () => ({
    showModal: false,
    showLogin: false,
    showRegister: false,
  }),
  actions: {
    openModal(value) {
      this.showModal = true;
      if (value === 'login') {
        this.showRegister = false
        this.showLogin = true;
      }
      if (value === 'register') {
        this.showLogin = false
        this.showRegister= true;
      }
    },
    closeModal() {
      this.showModal = false;
    },
  },
});