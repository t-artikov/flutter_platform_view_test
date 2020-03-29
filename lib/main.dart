import 'dart:async';

import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      showPerformanceOverlay: true,
      home: HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          AndroidView(viewType: 'gl_surface'),
          Center(
            child: AnimatedText('PlatformView test'),
          ),
        ],
      ),
    );
  }
}

class AnimatedText extends StatefulWidget {
  final String text;
  const AnimatedText(this.text);

  @override
  _AnimatedTextState createState() => _AnimatedTextState();
}

class _AnimatedTextState extends State<AnimatedText> {
  Timer timer;
  double x = -0.5;

  @override
  void initState() {
    super.initState();
    timer = Timer.periodic(Duration(seconds: 1), (timer) {
      _changePosition();
    });
  }

  @override
  void dispose() {
    timer.cancel();
    super.dispose();
  }

  void _changePosition() {
    setState(() {
      x = -x;
    });
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedAlign(
        alignment: Alignment(x, 0),
        child: Text(widget.text),
        duration: Duration(seconds: 1),
        curve: Curves.easeInOut);
  }
}
