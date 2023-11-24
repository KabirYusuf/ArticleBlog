<script setup>
import {ref} from 'vue';
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
                <label for="email" class="form__label">Email</label>
                <InputField id="email" type="email" placeholder="email@email.com" v-model:value="email"
                    inputClass="form__input--email" />

                <label for="password" class="form__label">Password</label>
                <InputField id="password" type="password" placeholder="Password" v-model:value="password" inputClass="form__input--password" />
                <Button type="button" @click="handleSubmit">Log in</Button>
        </form>
    </div>
</template>