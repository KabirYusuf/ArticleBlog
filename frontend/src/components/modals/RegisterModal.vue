<script setup>
import {ref} from 'vue';
import { useModalStore } from '../../store/modalStore';
import InputField from '../inputs/InputField.vue';
import Button from '../inputs/Button.vue'


const email = ref('')
const username = ref('')
const password = ref('')
const repeatPassword = ref('')

const handleSubmit = () => {
  
  console.log(email.value, password.value, username.value, repeatPassword.value)
  
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
                <Button type="button" @click="handleSubmit">Register</Button>
        </form>
  </div>
</template>