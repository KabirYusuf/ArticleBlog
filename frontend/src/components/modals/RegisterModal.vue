<script setup>
import { ref } from 'vue';
import { useModalStore } from '../../store/modalStore';
import InputField from '../inputs/InputField.vue';
import Button from '../inputs/Button.vue'
import { register } from '../../utility/Auth';


const email = ref('')
const name = ref('')
const username = ref('')
const password = ref('')
const repeatPassword = ref('')
const isPremium = ref(false);
const message = ref('')

const handleSubmit = async () => {
    console.log(email.value, password.value, username.value, repeatPassword.value, isPremium.value)
    message.value = ''
    if (password !== repeatPassword) {
        message.value = 'Password mismatch'
    }
    if (!email || !username || !password) {
        return
    }
    try {
        const response = await register({
            email: email.value,
            username: username.value,
            password: password.value
        });
        console.log(response.data)
    } catch (error) {
        console.log(error.message)
    }


}

const modalStore = useModalStore();

</script>
<template>
    <div v-if="modalStore.showRegister">
        <div class="modalContent__header">
            <p>Register</p>
        </div>

        <form @submit.prevent="handleSubmit">
            <label for="email" class="form__label">Email</label>
            <InputField id="email" type="email" placeholder="email@email.com" v-model:value="email" class="input__field" />

            <label for="name" class="form__label">Name</label>
            <InputField id="name" type="text" placeholder="name" v-model:value="name"
                class="input__field" />

            <label for="username" class="form__label">Username</label>
            <InputField id="username" type="text" placeholder="username" v-model:value="username"
                class="input__field" />

            <label for="password" class="form__label">Password</label>
            <InputField id="password" type="password" placeholder="Password" v-model:value="password"
                class="input__field" />

            <label for="repeat-password" class="form__label">Repeat Password</label>
            <InputField id="repeat-password" type="password" placeholder="Password" v-model:value="repeatPassword"
                class="input__field" />
            
            <InputField id="isPremium" type="checkbox" v-model:checked="isPremium" class="checkbox__field" />
            <label for="isPremium" class="form__label form__label--premium">I want Premium</label>
            
            
            <Button type="submit" class="form__submitButton">Register</Button>
        </form>
    </div>
</template>