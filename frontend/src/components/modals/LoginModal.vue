<script setup>
import { ref } from 'vue';
import { useModalStore } from '../../store/modalStore';
import InputField from '../inputs/InputField.vue'
import Button from '../inputs/Button.vue'
import {login} from '../../utility/Auth'

const modalStore = useModalStore();

const username = ref('')
const password = ref('')
const message = ref('')

const handleSubmit = async() => {
    console.log(username.value, password.value)
    
    message.value = ''
    if (!username || !password) {
        return
    }
    
    try {
        const response = await login({
            username: username.value,
            password: password.value
        });
        localStorage.setItem('token', response.data.token);
        console.log(response.data)
    } catch (error) {
        console.log(error)
    }

}

</script>
<template>
    
        <div class="modalContent__header">

            <p>Log in</p>

        </div>

        <form @submit.prevent="handleSubmit">
            <label for="username" class="form__label">Username</label>
            <InputField id="username" type="email" placeholder="username" v-model:value="username" class="input__field" />

            <label for="loginPassword" class="form__label">Password</label>
            <InputField id="loginPassword" type="password" placeholder="Password" v-model:value="password"
                class="input__field" />
            <Button type="submit"  class="form__submitButton">Log in</Button>
        </form>
    
</template>