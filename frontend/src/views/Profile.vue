<template>
  <div class="container">


    <div class="col-md-12">
    <div class="card card-container">
      <form name="form" @submit.prevent="preventGuess"> 
        <div class="form-group">
            <input
              type="text"
              class="form-control"
              name="selectedGuess"
              placeholder="Select 4 digits"
              v-model="sGuess"
              v-validate="'required|min:4|max:4'"
              readonly
            />
        </div>
        <table style="table-layout: fixed; margin: auto">
          <tr>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('1')">1</button></td>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('2')">2</button></td>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('3')">3</button></td>
          </tr>
          <tr>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('4')">4</button></td>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('5')">5</button></td>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('6')">6</button></td>
          </tr>
          <tr>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('7')">7</button></td>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('8')">8</button></td>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('9')">9</button></td>
          </tr>
          <tr>
              <td style="width: 33%"></td>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="digitClicked('0')">0</button></td>
              <td style="width: 33%"><button class="btn btn-primary btn-block" @click="clearGuess()">Clear</button></td>
          </tr>
        </table>
      </form>

      <div
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
        v-if="message"
      >{{message}}</div>
    </div>
  </div>



    <table class="table table-hover">
          <thead>
            <tr>
                <th scope="col">Selected guess</th>
                <th scope="col">Result</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="guess in content" :key="guess.id">
              <td>{{ guess.selectedGuess }}</td>
              
              <td v-if="guess.guessResult === '4Б0К'">
                <strong >
                  {{ guess.guessResult }}
                </strong>
              </td>

              <td v-else>
                  {{ guess.guessResult }}
              </td>
              
            </tr>
          </tbody>
        </table>
  </div>
</template>

<script>
import axios from 'axios';
import authHeader from '../services/auth-header.js';

const API_URL = 'http://localhost:8080/rest/';

export default {
  name: 'profile',
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  data() {
    return {
      content: '',
      sGuess: '',
      submitted: false,
      successful: false,
      message: ''
    };
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }

    this.getGuessList();
    
  },

    methods: {
      getGuessList() {
        axios.get(API_URL + 'main', { headers: authHeader() }).then(
          response => {
            if (response.data._embedded.guessList) {
            this.content = response.data._embedded.guessList;
            }
          },
          error => {
            this.content = error.response.data.message;
          }
        );
      },

      clearGuess() {
        this.sGuess = '';
      },

      digitClicked(dValue) {
        if (this.sGuess.indexOf(dValue) === -1) {
          this.sGuess = this.sGuess + dValue;
        }
        if (this.sGuess.length === 4) {
          this.handleGuess(this.sGuess);
        }
      },

      handleGuess(sGuess) {
        this.message = '';
        this.submitted = true;
        this.$validator.validate().then(valid => {
          if (valid) {
            axios.post(API_URL + 'main', {
            selectedGuess: sGuess}, { 
              headers: authHeader() }).then(
              data => {
                this.message = data.guessResult;
                this.successful = true;
                this.clearGuess();
                this.getGuessList();

              },
              error => {
                this.message = error.message;
                this.successful = false;
              }
            );
          }
        });
      },

      preventGuess() {
        this.$validator.validate();
      },

    }
};
</script>

<style scoped>
label {
  display: block;
  margin-top: 10px;
}

.card-container.card {
  max-width: 350px !important;
  padding: 40px 40px;
}

.card {
  background-color: #f7f7f7;
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  -moz-border-radius: 2px;
  -webkit-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}
</style>