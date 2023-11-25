<script setup>
import {ref} from 'vue';
import { useModalStore } from '../../store/modalStore';
import InputField from '../inputs/InputField.vue';
import Button from '../inputs/Button.vue'
import {register} from '../../utility/Auth';


const email = ref('')
const username = ref('')
const password = ref('')
const repeatPassword = ref('')
const message = ref('')

const handleSubmit = async () => {
    // console.log(email.value, password.value, username.value, repeatPassword.value)
    message.value = ''
    if (password !== repeatPassword) {
        message.value = 'Password mismatch'
    }
    if (!email || !username || !password) {
        return
    }
    try {
        const response = await register({email: email.value,
             username: username.value,
              password: password.value});
    console.log(response.data)
    } catch (error) {
        console.log(error.message)
    }

  
}

const modalStore = useModalStore();

</script>
<template>
    <div v-if="modalStore.showRegister" >
      <div class="modalContent__header">
        <p>Register</p>
      </div>  

      <form @submit.prevent="handleSubmit">
                <label for="email" class="form__label">Email</label>
                <InputField id="email" type="email" placeholder="email@email.com" v-model:value="email"
                    inputClass="form__input--email" />

                <label for="username" class="form__label">Username</label>
                <InputField id="username" type="username" placeholder="username" v-model:value="username"
                    inputClass="form__input--username" />

                <label for="password" class="form__label">Password</label>
                <InputField id="password" type="password" placeholder="Password" v-model:value="password" inputClass="form__input--password" />

                <label for="repeat-password" class="form__label">Repeat Password</label>
                <InputField id="repeat-password" type="password" placeholder="Password" v-model:value="repeatPassword" inputClass="form__input--password" />
                <Button type="submit">Register</Button>
        </form>
  </div>
</template>