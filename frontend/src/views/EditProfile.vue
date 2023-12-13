<template>
    <ProfileHeader>
        <div class="myProfileHeader__content">
            <div class="myProfileHeader__inner">

                <img @click="toggleUploader" :src="userImageUrl" alt="profile-image" class="profileHeader__image"
                    style="cursor: pointer;">

                <ImageCropUpload ref="formData.image" :width="200" :height="200" :preview="true" lang-type="en"
                    v-model="showUploader" :url="uploadUrl" :params="params" :headers="headers" :img-format="imgFormat"
                    @crop-success="cropSuccess" @crop-upload-success="cropUploadSuccess"
                    @crop-upload-fail="cropUploadFail" />
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
            <p style="color: red;" v-if="firstNameError" class="error-message">{{ firstNameError }}</p>

            <label for="lastName" class="form__label">Last Name</label>
            <InputField id="lastNname" type="text" placeholder="last name" v-model:value="lastName" class="input__field" />
            <p style="color: red;" v-if="lastNameError" class="error-message">{{ lastNameError }}</p>

            <label for="password" class="form__label">Password</label>
            <InputField id="password" type="password" placeholder="Password" v-model:value="password"
                class="input__field" />

            <label for="repeat-password" class="form__label">Repeat Password</label>
            <InputField id="repeat-password" type="password" placeholder="Password" v-model:value="repeatPassword"
                class="input__field" />
            <p style="color: red;" v-if="passwordError" class="error-message">{{ passwordError }}</p>

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
import { ref, onMounted, computed } from 'vue';
import { useUserStore } from '../store/userStore';
import { updateUser } from '../utility/userApiService';
import Swal from 'sweetalert2'
import { useRouter } from 'vue-router';
import ImageCropUpload from 'vue-image-crop-upload';
import { handleErrors } from '../utility/handleErrors';



const userStore = useUserStore();
const router = useRouter();


const firstName = ref('')
const lastName = ref('')
const password = ref('')
const repeatPassword = ref('')
const profileImageData = ref('');

const firstNameError = ref('');
const lastNameError = ref('');
const passwordError = ref('');


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

    firstNameError.value = '';
    lastNameError.value = '';
    passwordError.value = '';

    let isFormInvalid = false;
    if (!firstName.value) {
        firstNameError.value = 'First Name cannot be empty';
        isFormInvalid = true;
    }
    if (!lastName.value) {
        lastNameError.value = 'Last Name cannot be empty';
        isFormInvalid = true;
    }

    if (isFormInvalid) {
        return;
    }

    if (password.value !== repeatPassword.value) {
        passwordError.value = 'Password mismatch';
        return;
    }

    try {
        const response = await updateUser({
            password: password.value,
            firstName: firstName.value,
            lastName: lastName.value,
            userImage: profileImageData.value
        }, userStore.user.id);

        await Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'You have successfully updated your account'
        });

        router.push('/my-profile');

    } catch (error) {
        handleErrors(error, {
            firstName: firstNameError,
            lastName: lastNameError,
            password: passwordError
        });
    }

}

const showUploader = ref(false);
const imgDataUrl = ref('');
const uploadUrl = '/upload';
const imgFormat = 'png';
const params = ref({});
const headers = ref({});

const toggleUploader = () => {
    showUploader.value = !showUploader.value;
}

const cropSuccess = (imgDataUrlParam, field) => {
    imgDataUrl.value = imgDataUrlParam;

    const base64Image = imgDataUrlParam.replace(/^data:image\/[a-z]+;base64,/, '');

    profileImageData.value = base64Image;
    showUploader.value = false;
}

const cropUploadSuccess = (jsonData, field) => {
    console.log('Upload success:', jsonData);
}

const cropUploadFail = (status, field) => {
    console.error('Upload fail:', status);
}

const userImageUrl = computed(() => {
    return userStore.user?.userImage || '../../public/profile-avatar.webp';
});

</script>