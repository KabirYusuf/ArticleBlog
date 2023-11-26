<script setup>
import { ref } from 'vue';
import { useModalStore } from '../../store/modalStore';
import InputField from '../inputs/InputField.vue'
import Button from '../inputs/Button.vue'

const modalStore = useModalStore();

const email = ref('')
const password = ref('')

const handleSubmit = () => {
    console.log(email.value, password.value)

}

</script>
<template>
    <div v-if="modalStore.showLogin">
        <div class="modalContent__header">

            <p>Log in</p>

        </div>

        <form @submit.prevent="handleSubmit">
            <label for="loginEmail" class="form__label">Email</label>
            <InputField id="loginEmail" type="email" placeholder="email@email.com" v-model:value="email" class="input__field" />

            <label for="loginPassword" class="form__label">Password</label>
            <InputField id="loginPassword" type="password" placeholder="Password" v-model:value="password"
                class="input__field" />
            <Button type="button" @click="handleSubmit" class="form__submitButton">Log in</Button>
        </form>
    </div>
</template>