<script setup>
import {ref} from 'vue';
import {useModalStore} from '@/store/modalStore';
import InputField from '../inputs/InputField.vue'
import Button from '../inputs/Button.vue'
import {login} from '@/utility/Auth'
import Swal from 'sweetalert2'
import {useUserStore} from '@/store/userStore'
import {useRouter} from 'vue-router';
import {handleErrors} from '@/utility/handleErrors';
import SocialButton from "@/components/socials/SocialButton.vue";
import FormTemplate from "@/components/modals/FormTemplate.vue";

const userStore = useUserStore();
const router = useRouter();
const modalStore = useModalStore();

const username = ref('')
const password = ref('')
const usernameError = ref('')
const passwordError = ref('')
const message = ref('');

const handleChange = () => {
    usernameError.value = ''
    passwordError.value = ''
    message.value = ''
}

const handleSubmit = async () => {

    if (!username.value || !password.value) {
        message.value = 'username/password cant be empty'
        return;
    }

    try {
        const response = await login({
            username: username.value,
            password: password.value
        });

        localStorage.setItem('token', response.data.token);
        userStore.logIn();


        Swal.fire({
            icon: 'success',
            title: 'Logged In',
            text: 'You have successfully logged in'
        });

        modalStore.closeModal();

        router.push('/');

    } catch (error) {
        const message = error.response.data.Message; // Temporary solution
        if (error.response.status === 401 && message.includes('User is disabled')) {
            modalStore.closeModal();
            modalStore.openModal("emailVerification")
        }

        handleErrors(error, {
            username: usernameError,
            password: passwordError
        });
    }
};


</script>
<template>
    <div class="modalContent__header">

        <p>Log in</p>

    </div>
    <p v-if="message" style="color: red"> {{ message }} </p>


    <FormTemplate>
        <form @submit.prevent="handleSubmit">
            <label for="username" class="form__label">Username</label>
            <InputField id="username" type="text" placeholder="username" v-model:value="username"
                        class="input__field"
                        :focus="handleChange"/>
            <p v-if="usernameError" style="color: red;">{{ usernameError }}</p>

            <label for="loginPassword" class="form__label">Password</label>
            <InputField id="loginPassword" type="password" placeholder="Password" v-model:value="password"
                        class="input__field"
                        :focus="handleChange"/>
            <p v-if="passwordError" style="color: red;">{{ passwordError }}</p>
            <Button type="submit" class="form__submitButton">Log in</Button>
        </form>
    </FormTemplate>
</template>
