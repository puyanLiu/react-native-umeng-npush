/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  TouchableOpacity,
} from 'react-native';
import UmengPush from '@cbridge/react-native-umeng-npush';

// 获取DeviceToken
UmengPush.getDeviceToken((deviceToken) => {
  console.log('deviceToken: ', deviceToken);
});

// // 接收到推送消息回调
UmengPush.didReceiveMessage((message) => {
  console.log('didReceiveMessage:', message);
});

// 点击推送消息打开应用回调
UmengPush.didOpenMessage((message) => {
  console.log('didOpenMessage:', message);
});


const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});


type Props = {};
export default class App extends Component<Props> {

  
  render() {
    return (
      <View style={styles.container}>
      <TouchableOpacity onPress={() => {
          console.log('点击');
          UmengPush.getDeviceToken((deviceToken) => {
            console.log('deviceToken: ', deviceToken);
          });
        }}>
          <Text>点击</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => {
          console.log('添加别名');
          UmengPush.addAlias("13430264654", "userPhone", (isSuccess, message) => {
            console.log("是否成功", isSuccess, message);
          });

          UmengPush.addAlias("1", "userId", (isSuccess, message, info) => {
            console.log("是否成功", isSuccess, message, info);
          });
        }}>
          <Text>添加别名</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => {
          console.log('移除别名');
          UmengPush.deleteAlias("13430264654", "userPhone", (isSuccess, message) => {
            console.log("是否成功", isSuccess, message);
          });

          UmengPush.addAlias("1", "userId", (isSuccess, message, info) => {
            console.log("是否成功", isSuccess, message, info);
          });
        }}>
          <Text>移除别名</Text>
        </TouchableOpacity>

        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit App.js
        </Text>
        <Text style={styles.instructions}>
          {instructions}
        </Text>
        
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
