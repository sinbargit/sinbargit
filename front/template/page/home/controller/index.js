import ReactDom from 'react-dom';
import React from 'react';

class Root extends React.Component{
    constructor(props)
    {
        super(props);
    }
    render(){
        return (<div class="container"><aside>{this.props.tree}</aside><article>{this.props.article}</article></div>);
    }
}