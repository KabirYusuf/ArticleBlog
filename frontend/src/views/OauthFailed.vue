<template>
    <div class="error">
        <div class="error__content">
            <h1>Authentication Failed</h1>
            <p>Sorry, there was an issue with your authentication. Please try again later.</p>
        </div>
        <router-link to="/" class="error__btn">Go Back</router-link>
    </div>

</template>

<style scoped>
h1 {
    color: #D4A373;
}

p {
    color: #495057;
}

.error {
    text-align: center;
    margin-top: 30vh;
}

.error__content {
    padding-bottom: 30px;
}

.error__btn {
    padding: 10px 20px;
    background-color: #d2a679;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    text-decoration: none;
    transition: background-color 0.3s ease;
}

.error__btn:hover {
    background-color: #3E3E3E;
}
</style>
