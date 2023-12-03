<template>
    <ProfileHeader>
        <div class="myProfileHeader__content">
            <div class="myProfileHeader__inner">
                <ImageUploader/>
                <h2 class="profile__name" v-if="fullName">{{ fullName }}</h2>
                <h2 class="profile__name" v-else>{{ userStore.user?.username }}</h2>
                <p class="profile__email">{{ userStore.user?.email }}</p>
                <router-link to="/my-profile" class="profile__link">Back to profile</router-link>
                <router-link to="/my-profile" class="profile__link">Manage subscription</router-link>
            </div>
        </div>
    </ProfileHeader>

    <section class="editProfile__form">

        <p class="editProfile__title">Edit Profile</p>

        <form @submit.prevent="handleSubmit">
            <label for="firstName" class="form__label">First Name</label>
            <InputField id="firstNname" type="text" placeholder="first name" v-model:value="firstName"
                class="input__field" />

            <label for="lastName" class="form__label">Last Name</label>
            <InputField id="lastNname" type="text" placeholder="last name" v-model:value="lastName" class="input__field" />

            <label for="password" class="form__label">Password</label>
            <InputField id="password" type="password" placeholder="Password" v-model:value="password"
                class="input__field" />

            <label for="repeat-password" class="form__label">Repeat Password</label>
            <InputField id="repeat-password" type="password" placeholder="Password" v-model:value="repeatPassword"
                class="input__field" />

            <Button type="submit" class="form__submitButton">Update</Button>
        </form>
    </section>

    <Footer :isConnection="true" />
</template>

<script setup>
import ProfileHeader from '../components/layouts/ProfileHeader.vue';
import InputField from '../components/inputs/InputField.vue';
import Button from '../components/inputs/Button.vue';
import Footer from '../components/layouts/Footer.vue';
import ImageUploader from '../components/ImageUploader.vue';
import { ref, onMounted, computed } from 'vue';
import { useUserStore } from '../store/userStore';
import { updateUser } from '../utility/userApiService';
import Swal from 'sweetalert2'
import { useRouter } from 'vue-router';



const userStore = useUserStore();
const router = useRouter();


const firstName = ref('')
const lastName = ref('')
const password = ref('')
const repeatPassword = ref('')
const message = ref('')


onMounted(async () => {
    await userStore.fetchUser();
    if (userStore.user) {
        firstName.value = userStore.user.firstName || '';
        lastName.value = userStore.user.lastName || '';
    }
});

const fullName = computed(() => {
    if (userStore.user && (userStore.user.firstName || userStore.user.lastName)) {
        return [userStore.user.firstName, userStore.user.lastName].filter(Boolean).join(' ');
    }
    return '';
});


const handleSubmit = async () => {
    message.value = '';


    if (!password.value || !firstName.value || !lastName.value) {
        message.value = 'Please fill in all fields';

        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: message.value
        });
        return;
    }

    if (password.value !== repeatPassword.value) {
        message.value = 'Password mismatch';
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: message.value
        });
        return;
    }

    try {
        const response = await updateUser({
            password: password.value,
            firstName: firstName.value,
            lastName: lastName.value
        }, userStore.user.id);

        await Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'You have successfully updated your account'
        });

        router.push('/my-profile');
        
    } catch (error) {
        const errorMessage = error.response?.data?.errors
            ? Object.values(error.response.data.errors)[0]
            : 'An error occurred';
        Swal.fire({
            icon: 'error',
            title: 'Unsuccessful',
            text: errorMessage
        });
    }
}


</script>