<template>
  <div class="auth-container">
    <v-container class="d-flex justify-center align-center fill-height">
      <v-card class="glass-card pa-6" width="400">
        <h2 class="text-center mb-4 gradient-text">Sign Up</h2>
        
        <v-form @submit.prevent="signup">
          <v-text-field
            variant="outlined"
            rounded="xl"
            prepend-icon="mdi-account"
            v-model="username"
            label="Username"
            required
          ></v-text-field>

          <v-text-field
            variant="outlined"
            rounded="xl"
            prepend-icon="mdi-email"
            v-model="email"
            label="Email"
            required
          ></v-text-field>

          <v-text-field
            variant="outlined"
            rounded="xl"
            prepend-icon="mdi-lock"
            v-model="password"
            label="Password"
            type="password"
            required
          ></v-text-field>

          <v-btn elevation="6" rounded="xl" type="submit" color="green" block class="mt-3">
            Sign Up
          </v-btn>

          <v-alert v-if="message" type="success" class="mt-3">{{ message }}</v-alert>

          <v-btn color="green" variant="text" rounded="xl" class="mt-2 text-button" @click="$router.push('/login')">
            Already have an account? Login
          </v-btn>
        </v-form>
      </v-card>
    </v-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      email: '',
      password: '',
      message: '',
    };
  },
  methods: {
    async signup() {
      try {
        const response = await fetch('/req/signup', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username: this.username, email: this.email, password: this.password }),
        });

        if (response.ok) {
          this.message = 'Signup successful! Redirecting to login...';
          setTimeout(() => this.$router.push('/login'), 1500);
        } else {
          this.message = 'Signup failed.';
        }
      } catch (err) {
        this.message = 'Something went wrong. Try again.';
      }
    },
  },
};
</script>

<style scoped>
  .auth-container {
    background: linear-gradient(135deg, #e8f5e9, #ffffff);
    background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' version='1.1' xmlns:xlink='http://www.w3.org/1999/xlink' xmlns:svgjs='http://svgjs.dev/svgjs' width='1470' height='564.5' preserveAspectRatio='none' viewBox='0 0 1470 564.5'%3e%3cg clip-path='url(%26quot%3b%23SvgjsClipPath1617%26quot%3b)' fill='none'%3e%3crect width='1470' height='564.5' x='0' y='0' fill='url(%26quot%3b%23SvgjsLinearGradient1618%26quot%3b)'%3e%3c/rect%3e%3ccircle r='27.7' cx='1108.41' cy='60.65' fill='rgba(25%2c 135%2c 84%2c 1)'%3e%3c/circle%3e%3ccircle r='38.025' cx='414.45' cy='369.48' fill='rgba(25%2c 135%2c 84%2c 1)'%3e%3c/circle%3e%3ccircle r='46.845' cx='417.91' cy='295.88' fill='url(%26quot%3b%23SvgjsLinearGradient1619%26quot%3b)'%3e%3c/circle%3e%3ccircle r='26.515' cx='925.61' cy='152.1' fill='rgba(25%2c 135%2c 84%2c 1)'%3e%3c/circle%3e%3ccircle r='18.095' cx='701.27' cy='102.87' fill='url(%26quot%3b%23SvgjsLinearGradient1620%26quot%3b)'%3e%3c/circle%3e%3ccircle r='52.485' cx='733.45' cy='337.87' fill='url(%26quot%3b%23SvgjsLinearGradient1621%26quot%3b)'%3e%3c/circle%3e%3ccircle r='53.235' cx='110.21' cy='136.68' fill='rgba(25%2c 135%2c 84%2c 1)'%3e%3c/circle%3e%3ccircle r='45.595' cx='315.24' cy='437.16' fill='url(%26quot%3b%23SvgjsLinearGradient1622%26quot%3b)'%3e%3c/circle%3e%3ccircle r='54.455' cx='8.96' cy='309.92' fill='url(%26quot%3b%23SvgjsLinearGradient1623%26quot%3b)'%3e%3c/circle%3e%3ccircle r='55.06' cx='771.47' cy='476.4' fill='rgba(25%2c 135%2c 84%2c 1)'%3e%3c/circle%3e%3ccircle r='44.28' cx='1299.76' cy='118.86' fill='url(%26quot%3b%23SvgjsLinearGradient1624%26quot%3b)'%3e%3c/circle%3e%3ccircle r='40.815' cx='641.75' cy='133.98' fill='url(%26quot%3b%23SvgjsLinearGradient1625%26quot%3b)'%3e%3c/circle%3e%3ccircle r='40.08' cx='1225.02' cy='544.09' fill='url(%26quot%3b%23SvgjsLinearGradient1626%26quot%3b)'%3e%3c/circle%3e%3ccircle r='30.19' cx='2.81' cy='103.4' fill='url(%26quot%3b%23SvgjsLinearGradient1627%26quot%3b)'%3e%3c/circle%3e%3ccircle r='52.235' cx='1089.02' cy='237.1' fill='rgba(25%2c 135%2c 84%2c 1)'%3e%3c/circle%3e%3c/g%3e%3cdefs%3e%3cclipPath id='SvgjsClipPath1617'%3e%3crect width='1470' height='564.5' x='0' y='0'%3e%3c/rect%3e%3c/clipPath%3e%3clinearGradient x1='15.4%25' y1='-40.1%25' x2='84.6%25' y2='140.1%25' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1618'%3e%3cstop stop-color='rgba(232%2c 250%2c 234%2c 1)' offset='0'%3e%3c/stop%3e%3cstop stop-color='rgba(255%2c 255%2c 255%2c 1)' offset='1'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='324.22' y1='295.88' x2='511.6' y2='295.88' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1619'%3e%3cstop stop-color='rgba(125%2c 242%2c 124%2c 1)' offset='0.1'%3e%3c/stop%3e%3cstop stop-color='rgba(103%2c 134%2c 98%2c 1)' offset='0.9'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='665.0799999999999' y1='102.87' x2='737.4599999999999' y2='102.87' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1620'%3e%3cstop stop-color='rgba(189%2c 230%2c 180%2c 1)' offset='0.11'%3e%3c/stop%3e%3cstop stop-color='rgba(70%2c 135%2c 78%2c 1)' offset='0.88'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='628.48' y1='337.87' x2='838.4200000000001' y2='337.87' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1621'%3e%3cstop stop-color='rgba(189%2c 230%2c 180%2c 1)' offset='0.11'%3e%3c/stop%3e%3cstop stop-color='rgba(70%2c 135%2c 78%2c 1)' offset='0.88'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='224.05' y1='437.16' x2='406.43' y2='437.16' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1622'%3e%3cstop stop-color='rgba(50%2c 93%2c 58%2c 1)' offset='0.1'%3e%3c/stop%3e%3cstop stop-color='rgba(66%2c 136%2c 68%2c 1)' offset='0.9'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='-99.94999999999999' y1='309.92' x2='117.87' y2='309.92' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1623'%3e%3cstop stop-color='rgba(133%2c 224%2c 132%2c 1)' offset='0.1'%3e%3c/stop%3e%3cstop stop-color='rgba(70%2c 143%2c 94%2c 1)' offset='0.9'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='1211.2' y1='118.86' x2='1388.3200000000002' y2='118.86' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1624'%3e%3cstop stop-color='rgba(189%2c 230%2c 180%2c 1)' offset='0.11'%3e%3c/stop%3e%3cstop stop-color='rgba(70%2c 135%2c 78%2c 1)' offset='0.88'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='560.12' y1='133.98' x2='723.38' y2='133.98' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1625'%3e%3cstop stop-color='rgba(133%2c 224%2c 132%2c 1)' offset='0.1'%3e%3c/stop%3e%3cstop stop-color='rgba(70%2c 143%2c 94%2c 1)' offset='0.9'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='1144.86' y1='544.09' x2='1305.1799999999998' y2='544.09' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1626'%3e%3cstop stop-color='rgba(133%2c 224%2c 132%2c 1)' offset='0.1'%3e%3c/stop%3e%3cstop stop-color='rgba(70%2c 143%2c 94%2c 1)' offset='0.9'%3e%3c/stop%3e%3c/linearGradient%3e%3clinearGradient x1='-57.57' y1='103.4' x2='63.190000000000005' y2='103.4' gradientUnits='userSpaceOnUse' id='SvgjsLinearGradient1627'%3e%3cstop stop-color='rgba(133%2c 224%2c 132%2c 1)' offset='0.1'%3e%3c/stop%3e%3cstop stop-color='rgba(70%2c 143%2c 94%2c 1)' offset='0.9'%3e%3c/stop%3e%3c/linearGradient%3e%3c/defs%3e%3c/svg%3e");
    background-size: cover;
    background-size: cover;
    background-position: center;
    background-attachment: fixed;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;

  }
.glass-card {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(12px);
  border-radius: 15px;
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.glass-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.gradient-text {
  background: linear-gradient(45deg, #3bb371, #015b07);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.text-button {
  text-transform: none;
}
</style>
