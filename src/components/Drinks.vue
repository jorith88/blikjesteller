<template>
  <div v-for="(blikje) in blikjes" :key="blikje.id" class="mdl-card mdl-cell mdl-shadow--2dp">
      <div class="mdl-card__title">
          <h5>
              <div class="mdl-card__title-text">{{blikje.name}}</div>
              <div class="mdl-card__subtitle-text">{{blikje.price}}</div>
          </h5>
      </div>
      <div class="mdl-card__supporting-text">
          <h1>{{blikje.amount}}</h1>
      </div>
      <div class="mdl-card__menu">
          <button v-on:click="minusOne(blikje)" class="mdl-button mdl-button--icon mdl-js-button mdl-js-ripple-effect">
              <i class="material-icons">remove</i>
          </button>
          <button v-on:click="plusOne(blikje)" class="mdl-button mdl-button--icon mdl-js-button mdl-js-ripple-effect">
              <i class="material-icons">add</i>
          </button>
      </div>
  </div>
  <div class="mdl-card mdl-cell mdl-shadow--2dp">
      <div class="mdl-card__title">
          <h5>
              <div class="mdl-card__title-text">Totaal</div>
          </h5>
      </div>
      <div class="mdl-card__supporting-text">
          <h1>{{totalAmount}}</h1>
      </div>
  </div>
</template>

<style scoped>
</style>

<script>
import { $axios } from "@baloise/vue-axios";

export default {
  name: 'drinks',
  data: function() {
    return {
      blikjes: null,
      stateChanged: false,
      totalAmount: 0
    }
  },
  mounted() {
    $axios.get("/config/blikjes.json").then((response) => {
      this.blikjes = response.data;
    });
  },
  methods: {
    plusOne: function(blikje) {
      blikje.amount += 1;
      this.updateTotal();
    },
    minusOne: function(blikje) {
      if (blikje.amount > 0) {
        blikje.amount -= 1;
        this.updateTotal();
      }
    },
    updateTotal: function() {
      this.stateChanged = true;
      var totalAmount = 0;

      this.blikjes.forEach(function(blikje) {
          totalAmount += (blikje.price * blikje.amount);
      });

      this.totalAmount = totalAmount;
    },
  },
}
</script>