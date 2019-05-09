const router = new VueRouter({
    routes: [
        {
            name: 'other',
            path: '*',
            redirect: '/',
        },
        {
            name: 'about',
            path: '/about',
            component: httpVueLoader('js/pages/about.vue'),
        },
        {
            name: 'equipment',
            path: '/equipment',
            component: httpVueLoader('js/pages/equipment.vue'),
        },
        {
            name: 'profile',
            path: '/profile',
            component: httpVueLoader('js/pages/profile.vue'),
        },
        {
            name: 'requests',
            path: '/requests',
            component: httpVueLoader('js/pages/requests.vue'),
        },
        {
            name: 'signin',
            path: '/signin',
            component: httpVueLoader('js/pages/signin.vue'),
        },
        {
            name: 'signup',
            path: '/signup',
            component: httpVueLoader('js/pages/signup.vue'),
        },
        {
            name: 'start',
            path: '/',
            component: httpVueLoader('js/pages/start.vue'),
        },
    ]
});
