<script setup>
import { ref } from 'vue';
import InputField from '../inputs/InputField.vue';
import Button from '../inputs/Button.vue';
import { register } from '../../utility/Auth';
import Swal from 'sweetalert2';
import { useUserStore } from '../../store/userStore';
import { useModalStore } from '../../store/modalStore';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const modalStore = useModalStore();
const router = useRouter();

const email = ref('');
const firstName = ref('');
const lastName = ref('');
const username = ref('');
const password = ref('');
const repeatPassword = ref('');
const isPremium = ref(false);
const message = ref('');
const passwordError = ref('')

const handleChange = () => {
    message.value = ''
    passwordError.value = ''
}

const handleSubmit = async () => {
    message.value = '';
    if (password.value !== repeatPassword.value) {
        passwordError.value = 'Password mismatch';
        return;
    }
    if (!email.value || !username.value || !password.value) {
        message.value = 'Please fill in all required fields';
        return;
    }
    try {
        const response = await register({
            email: email.value,
            username: username.value,
            password: password.value,
            firstName: firstName.value,
            lastName: lastName.value
        });
        localStorage.setItem('token', response.data.token);
        userStore.logIn();

        modalStore.closeModal();

        Swal.fire({
            icon: 'success',
            title: 'Success',
            text: response.data.message
        });

        router.push('/');

    } catch (error) {

        if (!error.response.data.errors) {
            Swal.fire({
                icon: 'error',
                title: 'Unsuccessful',
                text: error.response.data
            });
        } else
            if (error.response.data.errors['password']) {
                passwordError.value = error.response.data.errors['password'];
            }

    }
};
</script>

<template>
    <div class="modalContent__header">
        <p>Register</p>
    </div>

    <form @submit.prevent="handleSubmit">
        <label for="email" class="form__label">Email</label>
        <InputField id="email" type="email" placeholder="email@email.com" v-model:value="email" class="input__field" />

        <label for="firstName" class="form__label">First Name</label>
        <InputField id="firstNname" type="text" placeholder="first name" v-model:value="firstName" class="input__field" />

        <label for="lastName" class="form__label">Last Name</label>
        <InputField id="lastNname" type="text" placeholder="last name" v-model:value="lastName" class="input__field" />

        <label for="username" class="form__label">Username</label>
        <InputField id="username" type="text" placeholder="username" v-model:value="username" class="input__field" />

        <label for="password" class="form__label">Password</label>
        <InputField id="password" type="password" placeholder="Password" v-model:value="password" class="input__field"
            :focus="handleChange" />

        <label for="repeat-password" class="form__label">Repeat Password</label>
        <InputField id="repeat-password" type="password" placeholder="Password" v-model:value="repeatPassword"
            class="input__field" :focus="handleChange" />
        <p v-if="passwordError" style="color: red;">{{ passwordError }}</p>
        <p v-if="message" style="color: red;">{{ message }}</p>

        <InputField id="isPremium" type="checkbox" v-model:checked="isPremium" class="checkbox__field" />
        <label for="isPremium" class="form__label form__label--premium">I want Premium</label>


        <Button type="submit" class="form__submitButton">Register</Button>
    </form>
</template>