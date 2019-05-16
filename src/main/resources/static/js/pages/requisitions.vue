<template>
  <c-default-page :storage="storage">
    <b-container>
      <b-row>
        <b-col>
          <h1>Requisitions</h1>
        </b-col>
      </b-row>
      <b-row>
        <b-col cols="8">
          <b-form-group>
            <b-button variant="primary" block to="/requisitions/new">Create New</b-button>
          </b-form-group>
          <b-table small :items="requisitions" :fields="fields">
            <template slot="holderName" slot-scope="data">
              <b-link :to="'/holders/' + data.item.holderUUID">{{data.value}}</b-link>
            </template>
            <template slot="id" slot-scope="data">
              <b-link :to="'/requisitions/' + data.item.id">{{data.value}}</b-link>
            </template>
            <template slot="creationDate" slot-scope="data">{{formatDate(data.value) }}</template>
          </b-table>
        </b-col>
        <b-col>
          <h4>Filtering</h4>
          <b-form-group>
            <b-form-select v-model="filter.scope" :options="filterOptions"></b-form-select>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>
  </c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("js/components/c-default-page.vue")
  },
  props: ["storage"],
  data: function() {
    return {
      fields: {
        id: {
          label: "Id",
          sortable: true
        },
        holderName: {
          label: "Holder",
          sortable: true
        },
        description: {
          label: "Description",
          sortable: true
        },
        status: {
          label: "Status",
          sortable: true
        },
        creationDate: {
          label: "Creation Date",
          sortable: true
        },
        status: {
          label: "Status",
          sortable: true
        }
      },
      filterOptions: [
        //{value: 'none', text: "All"},
        { value: "all", text: "All" },
        { value: "user", text: "Only mine own" }
      ],
      requisitions: [],
      filter: {
        scope: "all"
      }
    };
  },
  methods: {
    formatDate: function(string) {
      let date = new Date(string);
      return date.getFullYear() + "-" + date.getMonth() + "-" + date.getDay();
    },
    loadPage: function() {
      if (!this.storage.user) {
        // TODO
        return;
      }

      if (this.filter.scope == "all") {
        this.loadAllRequisitions();
      } else if (this.filter.scope == "user") {
        this.loadOwnedRequisitions();
      }
    },
    loadAllRequisitions: function() {
      const self = this;

      this.$server
        .get("/requisitions/")
        .then(function(response) {
          self.requisitions = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    },
    loadOwnedRequisitions: function() {
      const self = this;
      this.$server
        .get("/accounts/" + this.storage.user.login + "/requisitions/")
        .then(function(response) {
          self.requisitions = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    }
  },
  mounted: function() {
    this.loadPage();
  },
  watch: {
    "storage.user": function(a, b) {
      // TODO не очень хорошо получается, что метод дёргается 2 раза, но пока так.
      if (!this.storage.user) {
        return;
      }

      if (["admin", "staff"].includes(this.storage.user.role)) {
        this.filter.scope = "all";
      } else {
        this.filter.scope = "user";
      }

      this.loadPage();
    },
    filter: {
      handler: function(a, b) {
        this.loadPage();
      },
      deep: true
    }
  }
};
</script>
