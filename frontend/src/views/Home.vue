<template>
  <div class="container">
      <table class="table table-hover">
          <thead>
            <tr>
                <th scope="col">User</th>
                <th scope="col">Average attempts number</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in content" :key="user.id">
              <td>{{ user.username }}</td>
              <td>{{ user.rating }}</td>
            </tr>
          </tbody>
        </table>
  </div>
</template>

<script>
import axios from 'axios';

const API_URL = 'http://localhost:8080/rest/';

export default {
  name: 'home',
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  data() {
    return {
      content: ''
    };
  },
  mounted() {
    return axios.get(API_URL + 'users').then(
      response => {
        if (response.data._embedded.userList) {
        this.content = response.data._embedded.userList;
        }
      },
      error => {
        this.content = error.response.data.message;
      }
    );
  }
};
</script>
