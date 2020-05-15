import { ApolloServer } from 'apollo-server';
import { ApolloGateway } from '@apollo/gateway';
import * as dotenv from 'dotenv';

dotenv.config();

const gateway = new ApolloGateway({
    serviceList: [
        {
            name: 'workflows',
            url: process.env.WORKFLOW_SEARCH_URL
        }
    ]
});

const server = new ApolloServer({
    gateway,
    // Disable subscriptions (not currently supported with ApolloGateway)
    subscriptions: false,
});

server.listen({port: process.env.PORT || 4000}).then(({ url }) => {
    console.log(`🚀 Server ready at ${url}`);
});
